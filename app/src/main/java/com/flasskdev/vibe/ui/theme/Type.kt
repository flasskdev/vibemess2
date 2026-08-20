package com.flasskdev.vibe.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ═══════════════════════════════════════════════════════
//  Vibe — Premium Typography System
//  Uses system default (Roboto/SF) with tight,
//  refined kerning mirroring Apple HIG conventions.
// ═══════════════════════════════════════════════════════

private val VibeFont = FontFamily.Default

val Typography = Typography(
    // ── Display ──
    displayLarge = TextStyle(
        fontFamily = VibeFont,
        fontWeight = FontWeight.Black,
        fontSize = 34.sp,
        lineHeight = 41.sp,
        letterSpacing = 0.37.sp
    ),
    displayMedium = TextStyle(
        fontFamily = VibeFont,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.36.sp
    ),

    // ── Headlines ──
    headlineLarge = TextStyle(
        fontFamily = VibeFont,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.2).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = VibeFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.15).sp
    ),

    // ── Title ──
    titleLarge = TextStyle(
        fontFamily = VibeFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.3).sp
    ),
    titleMedium = TextStyle(
        fontFamily = VibeFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.4).sp
    ),
    titleSmall = TextStyle(
        fontFamily = VibeFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.2).sp
    ),

    // ── Body ──
    bodyLarge = TextStyle(
        fontFamily = VibeFont,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.4).sp
    ),
    bodyMedium = TextStyle(
        fontFamily = VibeFont,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.2).sp
    ),
    bodySmall = TextStyle(
        fontFamily = VibeFont,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.1).sp
    ),

    // ── Labels ──
    labelLarge = TextStyle(
        fontFamily = VibeFont,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    labelMedium = TextStyle(
        fontFamily = VibeFont,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = VibeFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 13.sp,
        letterSpacing = 0.06.sp
    )
)