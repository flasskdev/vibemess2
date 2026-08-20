import os
from pathlib import Path

# Список папок, которые обычно засоряют структуру (если нужно сканировать абсолютно всё — сделай IGNORE_DIRS = set())
IGNORE_DIRS = {'.git', '.idea', 'pycache', 'build', '.gradle', 'venv', '.venv', 'node_modules', 'keepRules', 'res', 'gradle'}
OUTPUT_FILE = "project_structure.txt"

def build_tree(dir_path: Path, prefix: str = "", file_out=None) -> None:
    try:
        # Сортируем: сначала папки, затем файлы (по алфавиту)
        entries = sorted(
            [e for e in dir_path.iterdir() if e.name not in IGNORE_DIRS],
            key=lambda x: (not x.is_dir(), x.name.lower())
        )
    except PermissionError:
        line = f"{prefix}└── [Доступ запрещен]\n"
        print(line, end="")
        if file_out:
            file_out.write(line)
        return

    total = len(entries)
    for i, entry in enumerate(entries):
        is_last = (i == total - 1)
        connector = "└── " if is_last else "├── "
        display_name = entry.name + ("/" if entry.is_dir() else "")
        
        line = f"{prefix}{connector}{display_name}\n"
        print(line, end="")
        if file_out:
            file_out.write(line)

        # Рекурсивный вход в подпапку
        if entry.is_dir():
            new_prefix = prefix + ("    " if is_last else "│   ")
            build_tree(entry, new_prefix, file_out)

def main():
    # '.' означает текущую папку проекта (можно указать точный путь, например: Path(r"C:\MyProject"))
    target_directory = Path(".") 
    root_name = target_directory.resolve().name + "/\n"
    
    print(f"Сохраняем структуру проекта в файл: {OUTPUT_FILE} ...\n")
    print(root_name, end="")
    
    with open(OUTPUT_FILE, "w", encoding="utf-8") as f:
        f.write(root_name)
        build_tree(target_directory.resolve(), file_out=f)

    print(f"\nГотово! Полная иерархия сохранена в файл: {OUTPUT_FILE}")

main()