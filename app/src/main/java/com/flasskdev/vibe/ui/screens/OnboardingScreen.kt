package com.flasskdev.vibe.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasskdev.vibe.ui.components.*
import com.flasskdev.vibe.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.ui.graphics.vector.ImageVector

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector
)

val onboardingIcons = listOf(Icons.Outlined.Lock, Icons.Outlined.FlashOn, Icons.Outlined.Psychology, Icons.Outlined.Language, Icons.Outlined.Science)

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val strings = com.flasskdev.vibe.ui.theme.LocalVibeStrings.current
    val onboardingPages = remember(strings) {
        strings.onboardingPages.mapIndexed { index, pair ->
            OnboardingPage(pair.first, pair.second, onboardingIcons.getOrElse(index) { Icons.Outlined.AutoAwesome })
        }
    }

    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == onboardingPages.size - 1

    // One shared idle animation drives the hero tile on every page: a slow bob plus a
    // breathing glow, so a static screen never looks frozen while the user reads.
    val idle = rememberInfiniteTransition(label = "onboardingIdle")
    val bob by idle.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heroBob"
    )
    val glowPulse by idle.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heroGlow"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        VibeBackgroundMesh()

        Column(modifier = Modifier.fillMaxSize()) {

            // ─── Skip: available until the last page, then it fades out ───
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                this@Column.AnimatedVisibility(
                    visible = !isLastPage,
                    enter = fadeIn(tween(200)),
                    exit = fadeOut(tween(200))
                ) {
                    Text(
                        text = strings.onboardingSkip,
                        modifier = Modifier
                            .clip(RoundedCornerShape(percent = 50))
                            .clickable(onClickLabel = strings.onboardingSkip, onClick = onFinished)
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f)
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                pageSpacing = 0.dp,
                beyondViewportPageCount = 1
            ) { pageIndex ->
                val page = onboardingPages[pageIndex]
                val pageOffset = ((pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction)
                val focus = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 40.dp)
                        .semantics {
                            contentDescription = strings.a11yOnboardingPage(pageIndex + 1, onboardingPages.size)
                        }
                        .graphicsLayer {
                            translationX = pageOffset * size.width * 0.5f
                            alpha = focus
                            val scale = 0.9f + focus * 0.1f
                            scaleX = scale
                            scaleY = scale
                        }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        // Soft radial glow, breathing with the idle transition
                        Box(
                            modifier = Modifier
                                .size(300.dp)
                                .graphicsLayer {
                                    alpha = focus * 0.8f
                                    scaleX = 1.3f * glowPulse
                                    scaleY = 1.3f * glowPulse
                                }
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            VibePrimary.copy(alpha = 0.6f),
                                            VibePrimary.copy(alpha = 0.2f),
                                            Color.Transparent
                                        ),
                                        radius = 300f
                                    ),
                                    shape = CircleShape
                                )
                        )

                        // Hero tile: gradient hairline, real elevation, gentle bob
                        Box(
                            modifier = Modifier
                                .graphicsLayer {
                                    translationY = bob * focus
                                    rotationZ = pageOffset * 4f
                                }
                                .size(240.dp)
                                .shadow(
                                    elevation = 26.dp,
                                    shape = RoundedCornerShape(68.dp),
                                    ambientColor = VibePrimary.copy(alpha = 0.28f),
                                    spotColor = VibePrimary.copy(alpha = 0.35f)
                                )
                                .clip(RoundedCornerShape(68.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.surface,
                                            MaterialTheme.colorScheme.surface.copy(alpha = 0.86f)
                                        )
                                    )
                                )
                                .border(
                                    width = 1.dp,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.35f),
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(68.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            // Specular sheen across the top half of the tile
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.5f)
                                    .align(Alignment.TopCenter)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.White.copy(alpha = 0.10f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )

                            Icon(
                                imageVector = page.icon,
                                contentDescription = null,
                                tint = VibePrimary,
                                modifier = Modifier
                                    .size(96.dp)
                                    .graphicsLayer {
                                        // Parallax: the glyph trails behind its tile while swiping
                                        translationX = -pageOffset * 150f
                                        rotationZ = pageOffset * 10f
                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(56.dp))

                    Text(
                        text = page.title,
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.graphicsLayer { translationX = -pageOffset * 60f }
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = page.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.graphicsLayer { translationX = -pageOffset * 30f }
                    )
                }
            }

            // ─── Liquid page control ───
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(onboardingPages.size) { index ->
                    val offset = (pagerState.currentPage - index + pagerState.currentPageOffsetFraction).absoluteValue
                    val focus = (1f - offset).coerceIn(0f, 1f)

                    val width = 8.dp + (26.dp * focus)
                    val color = if (focus > 0f) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.28f + (0.72f * focus))
                    } else {
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.10f)
                    }

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .width(width)
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (focus > 0.5f) {
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            Color(0xFF5AC8FA)
                                        )
                                    )
                                } else {
                                    Brush.horizontalGradient(colors = listOf(color, color))
                                }
                            )
                            .clickable(onClickLabel = strings.a11yOnboardingPage(index + 1, onboardingPages.size)) {
                                scope.launch {
                                    pagerState.animateScrollToPage(
                                        page = index,
                                        animationSpec = spring(stiffness = Spring.StiffnessLow)
                                    )
                                }
                            }
                    )
                }
            }

            Box(modifier = Modifier.padding(horizontal = 32.dp, vertical = 40.dp)) {
                VibeButton(
                    text = if (isLastPage) strings.onboardingGetStarted else strings.continueBtn,
                    onClick = {
                        if (pagerState.currentPage < onboardingPages.size - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    page = pagerState.currentPage + 1,
                                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                                )
                            }
                        } else {
                            onFinished()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}