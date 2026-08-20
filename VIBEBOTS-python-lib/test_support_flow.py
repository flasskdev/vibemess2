"""Self-contained behavior checks for the Vibe support-bot diagnostic flows.

Run with: py -3 test_support_flow.py
The test stubs the external LLM client and never contacts the network.
"""

from __future__ import annotations

import os
import sys
import types

os.environ.setdefault("VIBE_BOT_TOKEN", "test-token")
os.environ.setdefault("MISTRAL_API_KEY", "test-key")

# Keep this verification independent from a locally installed Mistral SDK.
mistral_module = types.ModuleType("mistralai")
mistral_client_module = types.ModuleType("mistralai.client")


class FakeMistral:
    def __init__(self, *args, **kwargs):
        self.chat = types.SimpleNamespace(complete=lambda **_: None)


mistral_client_module.Mistral = FakeMistral
sys.modules.setdefault("mistralai", mistral_module)
sys.modules.setdefault("mistralai.client", mistral_client_module)

import test as support  # noqa: E402


class RecordingBot:
    def __init__(self):
        self.messages: list[tuple[int, str, object]] = []

    def send_message(self, user_id: int, text: str, reply_markup=None):
        self.messages.append((user_id, text, reply_markup))
        return {"ok": True}


support.bot = RecordingBot()
USER_ID = 101

support.start_diagnostic(USER_ID, "connection")
assert support.diagnostic_sessions[USER_ID].step_index == 0
assert "Шаг 1 из 3" in support.bot.messages[-1][1]

support.answer_diagnostic(USER_ID, "connection", "network", "Wi-Fi")
support.answer_diagnostic(USER_ID, "connection", "internet", "да")
support.answer_diagnostic(USER_ID, "connection", "delivery", "ожидание")

session = support.diagnostic_sessions[USER_ID]
assert session.answers == {
    "network": "Wi-Fi",
    "internet": "да",
    "delivery": "ожидание",
}
assert "остается в ожидании" in support.bot.messages[-1][1] or "остаётся в ожидании" in support.bot.messages[-1][1]

report = support.format_report(USER_ID)
assert "ЧЕРНОВИК ОБРАЩЕНИЯ" in report
assert "network: Wi-Fi" in report
assert support.contains_secret("token=very-secret-value")
assert not support.contains_secret("Сообщение остаётся с часами")

print("SUPPORT_FLOW_TEST_OK")
