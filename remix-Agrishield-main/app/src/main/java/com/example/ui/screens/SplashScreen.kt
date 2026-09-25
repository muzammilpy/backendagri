package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.util.AppStrings
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onAnimationFinish: () -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    // Animation States
    var startLogoAnimation by remember { mutableStateOf(false) }
    var startTextAnimation by remember { mutableStateOf(false) }
    var startExitFade by remember { mutableStateOf(false) }

    // Logo scale and alpha with overshoot/bounce
    val logoScale by animateFloatAsState(
        targetValue = if (startLogoAnimation) 1f else 0.35f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logo_scale"
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (startLogoAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = EaseOutCubic),
        label = "logo_alpha"
    )

    // Title and Tagline entrance animations
    val textAlpha by animateFloatAsState(
        targetValue = if (startTextAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = EaseOutCubic),
        label = "text_alpha"
    )

    val textOffsetY by animateFloatAsState(
        targetValue = if (startTextAnimation) 0f else 24f,
        animationSpec = tween(durationMillis = 500, easing = EaseOutCubic),
        label = "text_offset"
    )

    // Exit fade animation
    val screenAlpha by animateFloatAsState(
        targetValue = if (startExitFade) 0f else 1f,
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        label = "screen_alpha"
    )

    // Pulse glow animation around the shield emblem
    val infiniteTransition = rememberInfiniteTransition(label = "halo_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo_scale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo_alpha"
    )

    // Choreographed Animation Timeline
    LaunchedEffect(Unit) {
        // Step 1: Trigger Logo Entrance
        startLogoAnimation = true
        delay(250)

        // Step 2: Trigger Text and Tagline Entrance
        startTextAnimation = true
        delay(750)

        // Step 3: Trigger Fade Out Transition
        startExitFade = true
        delay(250)

        // Step 4: Notify navigation callback
        onAnimationFinish()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .alpha(screenAlpha)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A2E16), // Dark Emerald Forest Top
                        Color(0xFF1B5E20), // Canonical requested #1B5E20 Forest Green
                        Color(0xFF092412)  // Deep Botanical Green Base
                    )
                )
            )
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Ambient background glow accents
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height * 0.44f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x3586EFAC),
                        Color(0x184ADE80),
                        Color.Transparent
                    ),
                    center = center,
                    radius = size.width * 0.75f * pulseScale
                ),
                radius = size.width * 0.75f * pulseScale,
                center = center
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            // Animated Shield + Leaf Emblem Logo
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(160.dp)
                    .scale(logoScale)
                    .alpha(logoAlpha)
                    .testTag("splash_logo")
            ) {
                // Outer Pulse Ring
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .scale(pulseScale)
                        .alpha(pulseAlpha)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0x554ADE80), Color.Transparent)
                            ),
                            shape = CircleShape
                        )
                )

                // High-Fidelity Custom Shield + Leaf Vector Artwork
                AgriShieldVectorLogo(
                    modifier = Modifier.size(136.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Animated App Name & Tagline
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .offset(y = textOffsetY.dp)
                    .alpha(textAlpha)
            ) {
                // App Name "AgriShield"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Agri",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.2.sp,
                        modifier = Modifier.testTag("splash_title_prefix")
                    )
                    Text(
                        text = "Shield",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF86EFAC), // Luminous Mint Green
                        letterSpacing = 1.2.sp,
                        modifier = Modifier.testTag("splash_title_suffix")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Tagline Pill Badge
                Surface(
                    color = Color(0x30000000),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3586EFAC)),
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .testTag("splash_tagline")
                ) {
                    Text(
                        text = AppStrings.appTagline(language),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFDCFCE7),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp)
                    )
                }
            }
        }

        // Bottom Brand Security Badge
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
                .alpha(textAlpha)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x22000000))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .background(Color(0xFF4ADE80), CircleShape)
                )
                Text(
                    text = "AI Crop Defense System",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF86EFAC).copy(alpha = 0.85f),
                    letterSpacing = 0.8.sp
                )
            }
        }
    }
}

/**
 * High-definition Canvas rendering of the AgriShield Emblem:
 * - Rounded Shield contour with mint-to-emerald gradient stroke and emerald fill
 * - Dimensional "A" chevron apex with beveled light/shadow facets
 * - Sprouting organic crop leaf blade with central spine highlight
 */
@Composable
fun AgriShieldVectorLogo(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // 1. Outer Glow/Rim Shield
        val outerShieldPath = Path().apply {
            moveTo(w * 0.50f, h * 0.10f)
            cubicTo(w * 0.68f, h * 0.15f, w * 0.86f, h * 0.16f, w * 0.90f, h * 0.22f)
            cubicTo(w * 0.92f, h * 0.52f, w * 0.82f, h * 0.78f, w * 0.50f, h * 0.94f)
            cubicTo(w * 0.18f, h * 0.78f, w * 0.08f, h * 0.52f, w * 0.10f, h * 0.22f)
            cubicTo(w * 0.14f, h * 0.16f, w * 0.32f, h * 0.15f, w * 0.50f, h * 0.10f)
            close()
        }

        // Draw Outer Shield Gradient Fill
        drawPath(
            path = outerShieldPath,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFA7F3D0), // Mint Top
                    Color(0xFF34D399), // Emerald Mid
                    Color(0xFF059669)  // Deep Emerald Bottom
                ),
                start = Offset(w * 0.2f, h * 0.1f),
                end = Offset(w * 0.8f, h * 0.9f)
            )
        )

        // 2. Inner Shield Inset (Dark Jungle Green background for high contrast)
        val innerShieldPath = Path().apply {
            moveTo(w * 0.50f, h * 0.15f)
            cubicTo(w * 0.65f, h * 0.19f, w * 0.80f, h * 0.20f, w * 0.84f, h * 0.26f)
            cubicTo(w * 0.85f, h * 0.50f, w * 0.76f, h * 0.73f, w * 0.50f, h * 0.88f)
            cubicTo(w * 0.24f, h * 0.73f, w * 0.15f, h * 0.50f, w * 0.16f, h * 0.26f)
            cubicTo(w * 0.20f, h * 0.20f, w * 0.35f, h * 0.19f, w * 0.50f, h * 0.15f)
            close()
        }

        drawPath(
            path = innerShieldPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0A361E),
                    Color(0xFF041C0E)
                ),
                startY = h * 0.15f,
                endY = h * 0.88f
            )
        )

        // Inner Shield Outline Accent
        drawPath(
            path = innerShieldPath,
            color = Color(0xFF22C55E).copy(alpha = 0.75f),
            style = Stroke(width = w * 0.016f)
        )

        // 3. Dimensional "A" Chevron - Left Light Facet
        val leftFacetPath = Path().apply {
            moveTo(w * 0.50f, h * 0.22f)
            lineTo(w * 0.50f, h * 0.72f)
            lineTo(w * 0.42f, h * 0.74f)
            lineTo(w * 0.30f, h * 0.68f)
            lineTo(w * 0.50f, h * 0.22f)
            close()
        }
        drawPath(
            path = leftFacetPath,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFD1FAE5), Color(0xFF6EE7B7)),
                start = Offset(w * 0.3f, h * 0.22f),
                end = Offset(w * 0.5f, h * 0.74f)
            )
        )

        // Dimensional "A" Chevron - Right Deep Facet
        val rightFacetPath = Path().apply {
            moveTo(w * 0.50f, h * 0.22f)
            lineTo(w * 0.70f, h * 0.68f)
            lineTo(w * 0.58f, h * 0.74f)
            lineTo(w * 0.50f, h * 0.72f)
            lineTo(w * 0.50f, h * 0.22f)
            close()
        }
        drawPath(
            path = rightFacetPath,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFF34D399), Color(0xFF047857)),
                start = Offset(w * 0.5f, h * 0.22f),
                end = Offset(w * 0.7f, h * 0.74f)
            )
        )

        // "A" Chevron Inner Negative Space Triangle
        val innerTriangle = Path().apply {
            moveTo(w * 0.50f, h * 0.40f)
            lineTo(w * 0.41f, h * 0.58f)
            lineTo(w * 0.59f, h * 0.58f)
            close()
        }
        drawPath(path = innerTriangle, color = Color(0xFF072715))

        // 4. Vibrant Sprouting Crop Leaf (Overlapping the "A")
        val leafStem = Path().apply {
            moveTo(w * 0.27f, h * 0.64f)
            cubicTo(w * 0.32f, h * 0.56f, w * 0.38f, h * 0.51f, w * 0.46f, h * 0.47f)
        }
        drawPath(
            path = leafStem,
            color = Color(0xFF86EFAC),
            style = Stroke(width = w * 0.045f, cap = StrokeCap.Round)
        )

        // Leaf Blade Body
        val leafBladePath = Path().apply {
            moveTo(w * 0.38f, h * 0.54f)
            cubicTo(w * 0.44f, h * 0.42f, w * 0.54f, h * 0.32f, w * 0.73f, h * 0.28f)
            cubicTo(w * 0.70f, h * 0.42f, w * 0.58f, h * 0.56f, w * 0.48f, h * 0.59f)
            cubicTo(w * 0.42f, h * 0.61f, w * 0.38f, h * 0.57f, w * 0.38f, h * 0.54f)
            close()
        }

        drawPath(
            path = leafBladePath,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFBEF264), // Radiant Lime
                    Color(0xFF4ADE80), // Vibrant Green
                    Color(0xFF10B981)  // Emerald
                ),
                start = Offset(w * 0.38f, h * 0.28f),
                end = Offset(w * 0.73f, h * 0.59f)
            )
        )

        // Leaf Blade Center Vein Spine
        val leafVein = Path().apply {
            moveTo(w * 0.40f, h * 0.54f)
            cubicTo(w * 0.48f, h * 0.46f, w * 0.58f, h * 0.38f, w * 0.73f, h * 0.28f)
        }
        drawPath(
            path = leafVein,
            color = Color.White.copy(alpha = 0.9f),
            style = Stroke(width = w * 0.016f, cap = StrokeCap.Round)
        )

        // Leaf subtle lateral vein
        val leafLateralVein = Path().apply {
            moveTo(w * 0.50f, h * 0.45f)
            cubicTo(w * 0.55f, h * 0.42f, w * 0.60f, h * 0.42f, w * 0.64f, h * 0.43f)
        }
        drawPath(
            path = leafLateralVein,
            color = Color(0xFFF0FDF4).copy(alpha = 0.7f),
            style = Stroke(width = w * 0.012f, cap = StrokeCap.Round)
        )

        // 5. Shield Apex Spark
        val sparkPath = Path().apply {
            moveTo(w * 0.50f, h * 0.09f)
            lineTo(w * 0.52f, h * 0.12f)
            lineTo(w * 0.50f, h * 0.15f)
            lineTo(w * 0.48f, h * 0.12f)
            close()
        }
        drawPath(path = sparkPath, color = Color(0xFFF0FDF4))
    }
}
