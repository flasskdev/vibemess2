package com.flasskdev.vibe.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasskdev.vibe.ui.theme.VibePrimary
import com.flasskdev.vibe.ui.theme.VibeSurfaceVariantDark
import com.flasskdev.vibe.ui.theme.VibeSurfaceVariantLight
import com.flasskdev.vibe.utils.TextFormatting
import com.flasskdev.vibe.utils.TextFormatting.FormatType

/**
 * Renders formatted text with support for:
 * bold, italic, strikethrough, underline, monospace, links,
 * colored text, spoilers, quotes, @mentions, and collapsible sections.
 */
@Composable
fun FormattedText(
    text: String,
    modifier: Modifier = Modifier,
    baseColor: Color = MaterialTheme.colorScheme.onBackground,
    fontSize: TextUnit = 15.sp,
    lineHeight: TextUnit = 20.sp,
    maxLines: Int = Int.MAX_VALUE,
    onMentionClick: ((String) -> Unit)? = null,
    onProfileClick: ((Int, String) -> Unit)? = null,
    onShowToast: ((String) -> Unit)? = null,
    isMine: Boolean = false,
    interactive: Boolean = true
) {
    val context = LocalContext.current
    val isDark = MaterialTheme.colorScheme.background == com.flasskdev.vibe.ui.theme.VibeBackgroundDark

    val spans = remember(text) { TextFormatting.parse(text) }

    // Check for CUT (read more) markers (only in multi-line mode)
    val cutIndex = spans.indexOfFirst { it.type == FormatType.CUT }
    val hasCut = cutIndex != -1 && maxLines > 1
    var isExpanded by remember { mutableStateOf(false) }

    // Separate spans before and after cut
    val visibleSpans = if (hasCut && !isExpanded) {
        spans.subList(0, cutIndex)
    } else {
        spans.filter { it.type != FormatType.CUT }
    }

    Column(modifier = modifier.animateContentSize(animationSpec = tween(300))) {
        // Render quotes, links, spoilers separately (block-level for multi-line blocks)
        val inlineSpans = mutableListOf<TextFormatting.FormattedSpan>()

        for (span in visibleSpans) {
            when (span.type) {
                FormatType.QUOTE -> {
                    if (maxLines == 1) {
                        // In single-line preview mode, render quote inline
                        inlineSpans.add(span)
                    } else {
                        if (inlineSpans.isNotEmpty()) {
                            RenderInlineSpans(inlineSpans.toList(), baseColor, fontSize, lineHeight, maxLines, isDark, context, onMentionClick, onProfileClick, onShowToast, isMine, interactive)
                            inlineSpans.clear()
                        }
                        QuoteBlock(text = span.text, baseColor = baseColor, fontSize = fontSize)
                    }
                }
                FormatType.SPOILER -> {
                    if (span.text.contains('\n') && maxLines > 1) {
                        if (inlineSpans.isNotEmpty()) {
                            RenderInlineSpans(inlineSpans.toList(), baseColor, fontSize, lineHeight, maxLines, isDark, context, onMentionClick, onProfileClick, onShowToast, isMine, interactive)
                            inlineSpans.clear()
                        }
                        SpoilerBlock(text = span.text, baseColor = baseColor, fontSize = fontSize, interactive = interactive, maxLines = maxLines)
                    } else {
                        // Inline spoiler chip
                        inlineSpans.add(span)
                    }
                }
                else -> {
                    inlineSpans.add(span)
                }
            }
        }

        // Flush remaining inline spans
        if (inlineSpans.isNotEmpty()) {
            RenderInlineSpans(inlineSpans.toList(), baseColor, fontSize, lineHeight, maxLines, isDark, context, onMentionClick, onProfileClick, onShowToast, isMine, interactive)
        }

        // Read more / Collapse
        if (hasCut) {
            Text(
                text = if (isExpanded) "▲ Свернуть" else "▼ Читать дальше",
                color = VibePrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun RenderInlineSpans(
    spans: List<TextFormatting.FormattedSpan>,
    baseColor: Color,
    fontSize: TextUnit,
    lineHeight: TextUnit,
    maxLines: Int,
    isDark: Boolean,
    context: Context,
    onMentionClick: ((String) -> Unit)?,
    onProfileClick: ((Int, String) -> Unit)? = null,
    onShowToast: ((String) -> Unit)? = null,
    isMine: Boolean = false,
    interactive: Boolean = true
) {
    var showLinkDialog by remember { mutableStateOf<Pair<String, String>?>(null) }
    val highlightColor = if (isMine) Color.White else VibePrimary
    val inlineContentMap = remember(spans, isMine) { mutableMapOf<String, InlineTextContent>() }
    val textMeasurer = androidx.compose.ui.text.rememberTextMeasurer()
    val density = androidx.compose.ui.platform.LocalDensity.current

    val annotated = buildAnnotatedString {
        spans.forEachIndexed { index, span ->
            when (span.type) {
                FormatType.PLAIN -> {
                    withStyle(SpanStyle(color = baseColor, fontSize = fontSize)) {
                        append(span.text)
                    }
                }
                FormatType.BOLD -> {
                    withStyle(SpanStyle(color = baseColor, fontSize = fontSize, fontWeight = FontWeight.Bold)) {
                        append(span.text)
                    }
                }
                FormatType.ITALIC -> {
                    withStyle(SpanStyle(color = baseColor, fontSize = fontSize, fontStyle = FontStyle.Italic)) {
                        append(span.text)
                    }
                }
                FormatType.BOLD_ITALIC -> {
                    withStyle(SpanStyle(color = baseColor, fontSize = fontSize, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)) {
                        append(span.text)
                    }
                }
                FormatType.STRIKETHROUGH -> {
                    withStyle(SpanStyle(color = baseColor, fontSize = fontSize, textDecoration = TextDecoration.LineThrough)) {
                        append(span.text)
                    }
                }
                FormatType.UNDERLINE -> {
                    withStyle(SpanStyle(color = baseColor, fontSize = fontSize, textDecoration = TextDecoration.Underline)) {
                        append(span.text)
                    }
                }
                FormatType.MONOSPACE -> {
                    pushStringAnnotation(tag = "MONO", annotation = span.text)
                    withStyle(SpanStyle(
                        color = if (isMine) Color.White else baseColor,
                        fontSize = (fontSize.value - 1).sp,
                        fontFamily = FontFamily.Monospace,
                        background = if (isMine) Color.White.copy(alpha = 0.2f) else if (isDark) VibeSurfaceVariantDark else VibeSurfaceVariantLight
                    )) {
                        append(span.text)
                    }
                    pop()
                }
                FormatType.LINK -> {
                    val inlineId = "link_${index}"
                    val measuredTextWidthPx = textMeasurer.measure(
                        text = androidx.compose.ui.text.AnnotatedString(span.text),
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    ).size.width

                    val chipWidth = with(density) {
                        (measuredTextWidthPx.toDp() + 30.dp).toSp()
                    }
                    val chipHeight = 22.sp
                    inlineContentMap[inlineId] = InlineTextContent(
                        Placeholder(
                            width = chipWidth,
                            height = chipHeight,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                        )
                    ) {
                        InlineLinkChip(
                            anchorText = span.text,
                            url = span.url ?: "",
                            context = context,
                            isMine = isMine,
                            interactive = interactive
                        )
                    }
                    appendInlineContent(inlineId, alternateText = span.text)
                }
                FormatType.SPOILER -> {
                    val inlineId = "spoiler_${index}"
                    val measuredTextWidthPx = textMeasurer.measure(
                        text = androidx.compose.ui.text.AnnotatedString(span.text),
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = fontSize,
                            fontWeight = FontWeight.Normal
                        )
                    ).size.width

                    val spoilerWidth = with(density) {
                        (measuredTextWidthPx.toDp() + 8.dp).toSp()
                    }
                    val spoilerHeight = (fontSize.value + 6).sp
                    inlineContentMap[inlineId] = InlineTextContent(
                        Placeholder(
                            width = spoilerWidth,
                            height = spoilerHeight,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                        )
                    ) {
                        SpoilerBlock(
                            text = span.text,
                            baseColor = baseColor,
                            fontSize = fontSize,
                            interactive = interactive,
                            maxLines = maxLines
                        )
                    }
                    appendInlineContent(inlineId, alternateText = span.text)
                }
                FormatType.QUOTE -> {
                    withStyle(SpanStyle(
                        color = baseColor.copy(alpha = 0.85f),
                        fontSize = fontSize,
                        fontStyle = FontStyle.Italic
                    )) {
                        append(span.text)
                    }
                }
                FormatType.RAW_LINK -> {
                    pushStringAnnotation(tag = "RAW_LINK", annotation = span.url ?: "")
                    withStyle(SpanStyle(
                        color = highlightColor,
                        fontSize = fontSize,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.Underline
                    )) {
                        append(span.text)
                    }
                    pop()
                }
                FormatType.COLOR -> {
                    val effectiveIsDark = isDark || isMine
                    val adjustedColor = TextFormatting.smartContrast(span.color ?: "#FFFFFF", effectiveIsDark)
                    withStyle(SpanStyle(color = adjustedColor, fontSize = fontSize)) {
                        append(span.text)
                    }
                }
                FormatType.MENTION -> {
                    pushStringAnnotation(tag = "MENTION", annotation = span.username ?: "")
                    withStyle(SpanStyle(
                        color = highlightColor,
                        fontSize = fontSize,
                        fontWeight = FontWeight.SemiBold
                    )) {
                        append(span.text)
                    }
                    pop()
                }
                else -> {
                    withStyle(SpanStyle(color = baseColor, fontSize = fontSize)) {
                        append(span.text)
                    }
                }
            }
        }
    }

    if (!interactive) {
        androidx.compose.foundation.text.BasicText(
            text = annotated,
            inlineContent = if (inlineContentMap.isNotEmpty()) inlineContentMap else emptyMap(),
            style = androidx.compose.ui.text.TextStyle(
                color = baseColor,
                fontSize = fontSize,
                lineHeight = lineHeight
            ),
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
        return
    }

    if (inlineContentMap.isNotEmpty()) {
        var layoutResult by remember { mutableStateOf<androidx.compose.ui.text.TextLayoutResult?>(null) }
        androidx.compose.foundation.text.BasicText(
            text = annotated,
            inlineContent = inlineContentMap,
            style = androidx.compose.ui.text.TextStyle(
                color = baseColor,
                fontSize = fontSize,
                lineHeight = lineHeight
            ),
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { layoutResult = it },
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures { pos ->
                    layoutResult?.let { layout ->
                        val offset = layout.getOffsetForPosition(pos)
                        annotated.getStringAnnotations("MONO", offset, offset).firstOrNull()?.let { ann ->
                            val clip = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clip.setPrimaryClip(ClipData.newPlainText("code", ann.item))
                            onShowToast?.invoke("Скопировано")
                        }
                        annotated.getStringAnnotations("MENTION", offset, offset).firstOrNull()?.let { ann ->
                            val username = ann.item
                            onMentionClick?.invoke(username)
                            if (onProfileClick != null && username.isNotBlank()) {
                                onProfileClick(0, username)
                            }
                        }
                        annotated.getStringAnnotations("RAW_LINK", offset, offset).firstOrNull()?.let { ann ->
                            showLinkDialog = Pair(ann.item, ann.item)
                        }
                    }
                }
            }
        )
    } else {
        androidx.compose.foundation.text.ClickableText(
            text = annotated,
            style = androidx.compose.ui.text.TextStyle(
                color = baseColor,
                fontSize = fontSize,
                lineHeight = lineHeight
            ),
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            onClick = { offset ->
                annotated.getStringAnnotations("MONO", offset, offset).firstOrNull()?.let { ann ->
                    val clip = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clip.setPrimaryClip(ClipData.newPlainText("code", ann.item))
                    onShowToast?.invoke("Скопировано")
                }
                annotated.getStringAnnotations("MENTION", offset, offset).firstOrNull()?.let { ann ->
                    val username = ann.item
                    onMentionClick?.invoke(username)
                    if (onProfileClick != null && username.isNotBlank()) {
                        onProfileClick(0, username)
                    }
                }
                annotated.getStringAnnotations("RAW_LINK", offset, offset).firstOrNull()?.let { ann ->
                    showLinkDialog = Pair(ann.item, ann.item)
                }
            }
        )
    }

    if (showLinkDialog != null) {
        LinkConfirmationDialog(
            url = showLinkDialog!!.first,
            onDismiss = { showLinkDialog = null },
            onShowToast = onShowToast
        )
    }
}

/**
 * Inline link chip block — renders a link as a styled chip with favicon and background directly in text flow.
 */
@Composable
private fun InlineLinkChip(
    anchorText: String,
    url: String,
    context: Context,
    isMine: Boolean = false,
    interactive: Boolean = true
) {
    var showLinkDialog by remember { mutableStateOf(false) }
    val domain = try {
        java.net.URI(if (url.startsWith("http")) url else "https://$url").host ?: url
    } catch (_: Exception) { url }

    val faviconUrl = "https://www.google.com/s2/favicons?domain=${domain}&sz=32"
    val bgColor = if (isMine) Color.White.copy(alpha = 0.22f) else VibePrimary.copy(alpha = 0.14f)
    val textColor = if (isMine) Color.White else VibePrimary
    val clickMod = if (interactive) Modifier.clickable { showLinkDialog = true } else Modifier

    Row(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .then(clickMod)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        coil.compose.AsyncImage(
            model = coil.request.ImageRequest.Builder(context)
                .data(faviconUrl)
                .crossfade(true)
                .build(),
            contentDescription = domain,
            modifier = Modifier
                .size(14.dp)
                .clip(RoundedCornerShape(3.dp)),
            error = null
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = anchorText,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            softWrap = false
        )
    }

    if (showLinkDialog) {
        LinkConfirmationDialog(
            url = url,
            onDismiss = { showLinkDialog = false }
        )
    }
}

@Composable
private fun LinkConfirmationDialog(
    url: String,
    onDismiss: () -> Unit,
    onShowToast: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val fullUrl = if (url.startsWith("http")) url else "https://$url"
    val domain = try {
        java.net.URI(fullUrl).host ?: url
    } catch (_: Exception) { url }

    val domainStart = fullUrl.indexOf(domain)
    val annotatedUrl = buildAnnotatedString {
        if (domainStart >= 0) {
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), fontSize = 14.sp)) {
                append(fullUrl.substring(0, domainStart))
            }
            withStyle(SpanStyle(color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)) {
                append(domain)
            }
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), fontSize = 14.sp)) {
                append(fullUrl.substring(domainStart + domain.length))
            }
        } else {
            withStyle(SpanStyle(color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)) {
                append(fullUrl)
            }
        }
    }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Открыть ссылку?", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text("Вы переходите на:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = annotatedUrl,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = {
                onDismiss()
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl))
                    context.startActivity(intent)
                } catch (e: Exception) {
                    onShowToast?.invoke("Ошибка при открытии ссылки")
                }
            }) {
                Text("Перейти", color = VibePrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Отмена", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        }
    )
}

@Composable
fun QuoteBlock(
    text: String,
    baseColor: Color = MaterialTheme.colorScheme.onBackground,
    fontSize: TextUnit = 15.sp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(IntrinsicSize.Max)
                .background(VibePrimary, RoundedCornerShape(1.5.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = text,
                color = baseColor.copy(alpha = 0.85f),
                fontSize = fontSize,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
fun SpoilerBlock(
    text: String,
    baseColor: Color = MaterialTheme.colorScheme.onBackground,
    fontSize: TextUnit = 15.sp,
    interactive: Boolean = true,
    maxLines: Int = Int.MAX_VALUE
) {
    var isRevealed by remember { mutableStateOf(false) }
    val revealProgress = remember { androidx.compose.animation.core.Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    val clickMod = if (interactive) {
        Modifier.clickable {
            isRevealed = !isRevealed
            coroutineScope.launch {
                if (isRevealed) {
                    revealProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    )
                } else {
                    revealProgress.snapTo(0f)
                }
            }
        }
    } else Modifier

    Box(
        modifier = Modifier
            .padding(vertical = 1.dp)
            .clip(RoundedCornerShape(4.dp))
            .then(clickMod),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (revealProgress.value > 0.2f) baseColor.copy(alpha = revealProgress.value) else Color.Transparent,
            fontSize = fontSize,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
        if (revealProgress.value < 1f) {
            TelegramSpoilerNoise(
                modifier = Modifier.matchParentSize(),
                progress = revealProgress.value,
                baseColor = baseColor,
                isAnimated = interactive
            )
        }
    }
}

/**
 * Ultra high-performance Telegram-style sparkling particle noise overlay for spoilers.
 * Uses pre-computed particle coordinates and zero-allocation draw loop for 120 FPS smooth performance.
 */
@Composable
private fun TelegramSpoilerNoise(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    baseColor: Color = MaterialTheme.colorScheme.onSurface,
    isAnimated: Boolean = true
) {
    // Pre-allocated particle buffer: 80 particles x 6 properties
    val particleData = remember {
        val count = 80
        val data = FloatArray(count * 6)
        for (i in 0 until count) {
            val offset = i * 6
            val angle = (i * 137.5 * Math.PI / 180.0)
            data[offset] = (i * 37 % 100) / 100f                       // 0: x ratio
            data[offset + 1] = (i * 73 % 100) / 100f                   // 1: y ratio
            data[offset + 2] = 0.9f + (i % 3) * 0.4f                   // 2: radius dp
            data[offset + 3] = 0.4f + (i % 5) * 0.12f                  // 3: base alpha
            data[offset + 4] = (i % 3).toFloat()                        // 4: color type (0: white, 1: silver, 2: base)
            data[offset + 5] = angle.toFloat()                         // 5: burst angle
        }
        data
    }

    val phase = if (isAnimated) {
        val infiniteTransition = rememberInfiniteTransition(label = "spoiler_perf")
        val anim by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 600, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "phase"
        )
        anim
    } else {
        0.5f
    }

    androidx.compose.foundation.Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@Canvas

        val globalAlpha = (1f - progress).coerceIn(0f, 1f)
        if (globalAlpha <= 0f) return@Canvas

        // 1. Dark backing mask to completely obscure the text (0% see-through)
        drawRoundRect(
            color = Color(0xFF161618).copy(alpha = 0.92f * globalAlpha),
            size = size,
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )

        // 2. Fast particle drawing from precomputed buffer (O(1) CPU time per frame)
        val count = 80
        for (i in 0 until count) {
            val offset = i * 6
            val rx = particleData[offset]
            val ry = particleData[offset + 1]
            val radius = particleData[offset + 2].dp.toPx() * (1f - progress * 0.5f)
            val baseAlpha = particleData[offset + 3]
            val colorType = particleData[offset + 4].toInt()
            val burstAngle = particleData[offset + 5]

            // Lightweight jitter calculation without nested loops
            val jitterPhase = (phase + i * 0.08f) % 1f
            val jitterX = if (isAnimated) (jitterPhase - 0.5f) * 2.5.dp.toPx() else 0f
            val jitterY = if (isAnimated) (((jitterPhase * 1.7f) % 1f) - 0.5f) * 2.dp.toPx() else 0f

            val burstDist = progress * 24.dp.toPx()
            val burstX = if (progress > 0f) (kotlin.math.cos(burstAngle) * burstDist) else 0f
            val burstY = if (progress > 0f) (kotlin.math.sin(burstAngle) * burstDist) else 0f

            val px = (rx * w + jitterX + burstX).coerceIn(0f, w)
            val py = (ry * h + jitterY + burstY).coerceIn(0f, h)

            val pColor = when (colorType) {
                0 -> Color.White
                1 -> Color(0xFFD6D6DC)
                else -> baseColor
            }

            val twinkle = if (isAnimated) {
                0.5f + 0.5f * kotlin.math.sin((phase * 6.28f + i).toDouble()).toFloat()
            } else 0.8f

            val finalAlpha = (baseAlpha * (0.35f + twinkle * 0.65f) * globalAlpha).coerceIn(0f, 1f)

            drawCircle(
                color = pColor.copy(alpha = finalAlpha),
                radius = radius,
                center = androidx.compose.ui.geometry.Offset(px, py)
            )
        }
    }
}