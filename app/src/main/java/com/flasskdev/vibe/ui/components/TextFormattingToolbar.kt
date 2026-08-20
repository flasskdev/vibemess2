package com.flasskdev.vibe.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasskdev.vibe.ui.theme.VibePrimary
import com.flasskdev.vibe.ui.theme.VibeStrings
import com.flasskdev.vibe.utils.TextFormatting
import com.flasskdev.vibe.utils.TextFormatting.FormatType

/**
 * Custom text selection context menu that appears when text is selected in the input field.
 * Shows: Copy | Cut | Format (expandable sub-menu)
 * Format sub-menu: Bold, Italic, Strikethrough, Underline, Monospace, Link, Color, Spoiler, Quote
 */
@Composable
fun TextSelectionContextMenu(
    visible: Boolean,
    inputText: String,
    selectionStart: Int,
    selectionEnd: Int,
    strings: VibeStrings,
    onApplyFormat: (String) -> Unit,
    onDismiss: () -> Unit,
    onCopy: () -> Unit,
    onCut: () -> Unit
) {
    val context = LocalContext.current
    var showFormatMenu by remember { mutableStateOf(false) }
    var showLinkDialog by remember { mutableStateOf(false) }
    var showColorDialog by remember { mutableStateOf(false) }
    
    val hasSelection = selectionStart != selectionEnd && selectionStart >= 0 && selectionEnd >= 0

    AnimatedVisibility(
        visible = visible && hasSelection,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            // Main row: Copy | Cut | Format
            if (!showFormatMenu) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ContextMenuButton(
                        text = strings.formatCopy,
                        icon = Icons.Default.ContentCopy,
                        onClick = {
                            onCopy()
                            onDismiss()
                        }
                    )
                    
                    VerticalDivider()
                    
                    ContextMenuButton(
                        text = strings.formatCut,
                        icon = Icons.Default.ContentCut,
                        onClick = {
                            onCut()
                            onDismiss()
                        }
                    )
                    
                    VerticalDivider()
                    
                    ContextMenuButton(
                        text = strings.formatFormat,
                        icon = Icons.Default.TextFormat,
                        onClick = { showFormatMenu = true }
                    )
                }
            }
            
            // Format sub-menu
            AnimatedVisibility(
                visible = showFormatMenu,
                enter = expandHorizontally() + fadeIn(),
                exit = shrinkHorizontally() + fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .horizontalScroll(rememberScrollState())
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back button
                    FormatChip(
                        text = "←",
                        onClick = { showFormatMenu = false }
                    )
                    
                    FormatChip(
                        text = "B",
                        isBold = true,
                        onClick = {
                            applyFormat(inputText, selectionStart, selectionEnd, FormatType.BOLD, onApplyFormat)
                            onDismiss()
                        }
                    )
                    
                    FormatChip(
                        text = "I",
                        isItalic = true,
                        onClick = {
                            applyFormat(inputText, selectionStart, selectionEnd, FormatType.ITALIC, onApplyFormat)
                            onDismiss()
                        }
                    )
                    
                    FormatChip(
                        text = "S",
                        isStrikethrough = true,
                        onClick = {
                            applyFormat(inputText, selectionStart, selectionEnd, FormatType.STRIKETHROUGH, onApplyFormat)
                            onDismiss()
                        }
                    )
                    
                    FormatChip(
                        text = "U",
                        isUnderline = true,
                        onClick = {
                            applyFormat(inputText, selectionStart, selectionEnd, FormatType.UNDERLINE, onApplyFormat)
                            onDismiss()
                        }
                    )
                    
                    FormatChip(
                        text = "</>",
                        isMono = true,
                        onClick = {
                            applyFormat(inputText, selectionStart, selectionEnd, FormatType.MONOSPACE, onApplyFormat)
                            onDismiss()
                        }
                    )
                    
                    FormatChip(
                        text = "🔗",
                        onClick = { showLinkDialog = true }
                    )
                    
                    FormatChip(
                        text = "🎨",
                        onClick = { showColorDialog = true }
                    )
                    
                    FormatChip(
                        text = "||",
                        onClick = {
                            applyFormat(inputText, selectionStart, selectionEnd, FormatType.SPOILER, onApplyFormat)
                            onDismiss()
                        }
                    )
                    
                    FormatChip(
                        text = ">>",
                        onClick = {
                            applyFormat(inputText, selectionStart, selectionEnd, FormatType.QUOTE, onApplyFormat)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
    
    // Link URL dialog
    if (showLinkDialog) {
        var linkUrl by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showLinkDialog = false },
            title = { Text(strings.formatLink, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = linkUrl,
                    onValueChange = { linkUrl = it },
                    label = { Text(strings.formatLinkUrlHint) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VibePrimary,
                        cursorColor = VibePrimary
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (hasSelection && linkUrl.isNotBlank()) {
                        val selectedText = inputText.substring(
                            selectionStart.coerceIn(0, inputText.length),
                            selectionEnd.coerceIn(0, inputText.length)
                        )
                        val formatted = TextFormatting.wrapWithFormat(selectedText, FormatType.LINK, url = linkUrl)
                        val newText = inputText.substring(0, selectionStart.coerceIn(0, inputText.length)) +
                                formatted +
                                inputText.substring(selectionEnd.coerceIn(0, inputText.length))
                        onApplyFormat(newText)
                    }
                    showLinkDialog = false
                    onDismiss()
                }) {
                    Text("OK", color = VibePrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLinkDialog = false }) {
                    Text(strings.cancelBtn, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }
        )
    }
    
    // Color picker dialog
    if (showColorDialog) {
        var hexColor by remember { mutableStateOf("#FF5733") }
        AlertDialog(
            onDismissRequest = { showColorDialog = false },
            title = { Text(strings.formatTextColor, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = hexColor,
                        onValueChange = { hexColor = it },
                        label = { Text(strings.formatColorHint) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VibePrimary,
                            cursorColor = VibePrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Color preview
                    val previewColor = try {
                        Color(android.graphics.Color.parseColor(hexColor))
                    } catch (_: Exception) {
                        Color.Gray
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(strings.formatPreview + ": ", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(previewColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sample text", color = previewColor, fontWeight = FontWeight.SemiBold)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (hasSelection && hexColor.startsWith("#")) {
                        val selectedText = inputText.substring(
                            selectionStart.coerceIn(0, inputText.length),
                            selectionEnd.coerceIn(0, inputText.length)
                        )
                        val formatted = TextFormatting.wrapWithFormat(selectedText, FormatType.COLOR, hexColor = hexColor)
                        val newText = inputText.substring(0, selectionStart.coerceIn(0, inputText.length)) +
                                formatted +
                                inputText.substring(selectionEnd.coerceIn(0, inputText.length))
                        onApplyFormat(newText)
                    }
                    showColorDialog = false
                    onDismiss()
                }) {
                    Text("OK", color = VibePrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showColorDialog = false }) {
                    Text(strings.cancelBtn, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }
        )
    }
}

@Composable
private fun ContextMenuButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = VibePrimary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(20.dp)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f))
    )
}

@Composable
private fun FormatChip(
    text: String,
    isBold: Boolean = false,
    isItalic: Boolean = false,
    isStrikethrough: Boolean = false,
    isUnderline: Boolean = false,
    isMono: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
            textDecoration = when {
                isStrikethrough -> TextDecoration.LineThrough
                isUnderline -> TextDecoration.Underline
                else -> null
            },
            fontFamily = if (isMono) FontFamily.Monospace else null,
            color = VibePrimary
        )
    }
}

private fun applyFormat(
    inputText: String,
    selectionStart: Int,
    selectionEnd: Int,
    format: FormatType,
    onApplyFormat: (String) -> Unit
) {
    val start = selectionStart.coerceIn(0, inputText.length)
    val end = selectionEnd.coerceIn(0, inputText.length)
    if (start == end) return
    
    val selectedText = inputText.substring(start, end)
    val formatted = TextFormatting.wrapWithFormat(selectedText, format)
    val newText = inputText.substring(0, start) + formatted + inputText.substring(end)
    onApplyFormat(newText)
}

/**
 * Preview toolbar that shows a formatted preview of the input text.
 */
@Composable
fun InputPreviewBar(
    inputText: String,
    visible: Boolean,
    strings: VibeStrings
) {
    AnimatedVisibility(
        visible = visible && inputText.isNotEmpty() && TextFormatting.hasFormatting(inputText),
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(Icons.Default.Visibility, contentDescription = null, tint = VibePrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(strings.formatPreview, fontSize = 12.sp, color = VibePrimary, fontWeight = FontWeight.SemiBold)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(8.dp)
            ) {
                FormattedText(
                    text = inputText,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    maxLines = 5
                )
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
        }
    }
}
