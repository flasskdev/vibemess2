package com.flasskdev.vibe.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasskdev.vibe.ui.components.*
import com.flasskdev.vibe.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: String
)

val onboardingIcons = listOf("🔒", "⚡", "🧠", "🌐", "🧪")

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val strings = com.flasskdev.vibe.ui.theme.LocalVibeStrings.current
    val onboardingPages = remember(strings) {
        strings.onboardingPages.mapIndexed { index, pair ->
            OnboardingPage(pair.first, pair.second, onboardingIcons.getOrElse(index) { "✨" })
        }
    }
    
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        VibeBackgroundMesh()

        Column(modifier = Modifier.fillMaxSize()) {
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

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 40.dp)
                        .graphicsLayer {
                            translationX = pageOffset * size.width * 0.5f
                            alpha = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
                            val scale = 0.9f + (1f - pageOffset.absoluteValue.coerceIn(0f, 1f)) * 0.1f
                            scaleX = scale
                            scaleY = scale
                        }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        // Soft Radial Glow
                        Box(
                            modifier = Modifier
                                .size(300.dp)
                                .graphicsLayer {
                                    alpha = (1f - pageOffset.absoluteValue.coerceIn(0f, 1f)) * 0.8f
                                    scaleX = 1.3f
                                    scaleY = 1.3f
                                }
                                .background(
                                    brush = androidx.compose.ui.graphics.Brush.radialGradient(
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

                        Box(
                            modifier = Modifier
                                .size(240.dp)
                                .clip(RoundedCornerShape(64.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), RoundedCornerShape(64.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = page.icon,
                                fontSize = 100.sp,
                                modifier = Modifier.graphicsLayer {
                                    translationX = -pageOffset * 150f
                                    rotationZ = pageOffset * 10f
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(64.dp))

                    Text(
                        text = page.title,
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = page.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Liquid Page Control
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(onboardingPages.size) { index ->
                    val offset = (pagerState.currentPage - index + pagerState.currentPageOffsetFraction).absoluteValue
                    
                    val width = if (offset < 1f) {
                        8.dp + (24.dp * (1f - offset))
                    } else {
                        8.dp
                    }
                    
                    val color = if (offset < 1f) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f + (0.7f * (1f - offset)))
                    } else {
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                    }

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .width(width)
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }

            Box(modifier = Modifier.padding(horizontal = 32.dp, vertical = 40.dp)) {
                VibeButton(
                    text = if (pagerState.currentPage == onboardingPages.size - 1) "GET STARTED" else "CONTINUE",
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
