package com.example.ui.components

import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AppLanguage
import com.example.model.ThreatSeverity
import com.example.ui.theme.*
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SeverityBadge(
    severity: ThreatSeverity,
    modifier: Modifier = Modifier,
    language: AppLanguage = LocalAppLanguage.current
) {
    val (bgColor, textColor, icon) = when (severity) {
        ThreatSeverity.CRITICAL -> Triple(SoftCriticalCard, CriticalRed, Icons.Default.Warning)
        ThreatSeverity.HIGH -> Triple(SoftWarningCard, SoftWarningText, Icons.Default.PriorityHigh)
        ThreatSeverity.MODERATE -> Triple(SoftPurpleCard, SoftPurpleText, Icons.Default.Info)
        ThreatSeverity.LOW -> Triple(SoftGreenCard, SoftGreenText, Icons.Default.CheckCircle)
    }

    val label = AppStrings.severity(severity, language)

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .wrapContentWidth()
            .testTag("severity_badge_${severity.name.lowercase()}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "⚠️",
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

@Composable
fun CropSampleIllustration(
    sampleKey: String,
    modifier: Modifier = Modifier,
    customImageUri: String? = null
) {
    if (!customImageUri.isNullOrBlank()) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = customImageUri,
                contentDescription = "Uploaded Crop Leaf Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Subtle translucent gradient at bottom to indicate uploaded status
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f)),
                            startY = 60f
                        )
                    )
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = LeafGreen,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Photo Loaded",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        return
    }

    val (gradientColors, icon, title) = when (sampleKey) {
        "tomato_late_blight" -> Triple(
            listOf(Color(0xFF8B1E1E), Color(0xFFC84B31)),
            Icons.Default.Coronavirus,
            "Late Blight"
        )
        "rice_blast" -> Triple(
            listOf(Color(0xFF2E6F40), Color(0xFF68B2A0)),
            Icons.Default.Grass,
            "Rice Blast"
        )
        "maize_fall_armyworm" -> Triple(
            listOf(Color(0xFF99582A), Color(0xFFDDA15E)),
            Icons.Default.BugReport,
            "Armyworm"
        )
        "cotton_pink_bollworm" -> Triple(
            listOf(Color(0xFF6B4226), Color(0xFFBC6C25)),
            Icons.Default.PestControl,
            "Bollworm"
        )
        "wheat_rust" -> Triple(
            listOf(Color(0xFFB07D1E), Color(0xFFE9C46A)),
            Icons.Default.Grain,
            "Yellow Rust"
        )
        "auto_detect" -> Triple(
            listOf(Color(0xFF1B4332), Color(0xFF2D6A4F)),
            Icons.Default.AutoAwesome,
            "Auto-Detect"
        )
        else -> Triple(
            listOf(ForestGreenPrimary, LeafGreen),
            Icons.Default.Eco,
            "Crop Health"
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(gradientColors)),
        contentAlignment = Alignment.Center
    ) {
        // Decorative background geometric curves
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = size.minDimension * 0.45f,
                center = Offset(size.width * 0.8f, size.height * 0.3f)
            )
            drawCircle(
                color = Color.Black.copy(alpha = 0.15f),
                radius = size.minDimension * 0.35f,
                center = Offset(size.width * 0.2f, size.height * 0.8f)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(Color.White.copy(alpha = 0.25f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CropLeafScannerOverlay(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanner_animation")
    val scanProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scan_line"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val yOffset = size.height * scanProgress
        // Draw horizontal scanning laser beam
        drawLine(
            brush = Brush.horizontalGradient(
                listOf(
                    Color.Transparent,
                    MintSoft.copy(alpha = 0.8f),
                    LeafGreen,
                    MintSoft.copy(alpha = 0.8f),
                    Color.Transparent
                )
            ),
            start = Offset(0f, yOffset),
            end = Offset(size.width, yOffset),
            strokeWidth = 4.dp.toPx()
        )
        // Draw ambient scan glow
        drawRect(
            brush = Brush.verticalGradient(
                listOf(
                    LeafGreen.copy(alpha = 0.15f * (1f - (scanProgress - 0.5f) * (scanProgress - 0.5f) * 4)),
                    Color.Transparent
                ),
                startY = yOffset - 40.dp.toPx(),
                endY = yOffset + 40.dp.toPx()
            ),
            topLeft = Offset(0f, (yOffset - 40.dp.toPx()).coerceAtLeast(0f)),
            size = androidx.compose.ui.geometry.Size(size.width, 80.dp.toPx())
        )
    }
}

@Composable
fun AudioAdvisoryBar(
    title: String,
    content: String,
    isSpeaking: Boolean,
    onPlay: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SoftGreenCard,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoftGreenBorder),
        modifier = modifier
            .fillMaxWidth()
            .testTag("audio_advisory_bar")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(CleanGreenLime, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isSpeaking) Icons.Default.GraphicEq else Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = "Voice Advisory",
                    tint = CleanGreenDark,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Audio Advisory (Multilingual Voice)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CleanGreenDark
                )
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = CleanGreenPrimary,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { if (isSpeaking) onStop() else onPlay() },
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag("btn_audio_toggle")
            ) {
                Icon(
                    imageVector = if (isSpeaking) Icons.Default.StopCircle else Icons.Default.PlayCircleFilled,
                    contentDescription = if (isSpeaking) "Stop Audio" else "Play Audio",
                    tint = CleanGreenPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun GeospatialRadarCanvas(
    hotspotCount: Int,
    criticalCount: Int,
    modifier: Modifier = Modifier,
    onRadarTap: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "radar_sweep")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF132A1F), Color(0xFF081C15))
                )
            )
            .clickable { onRadarTap() }
            .testTag("geospatial_radar_canvas")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val maxRadius = (size.minDimension / 2f) - 20.dp.toPx()

            // Concentric Range Rings (1km, 3km, 5km)
            val ringCount = 4
            for (i in 1..ringCount) {
                val r = (maxRadius / ringCount) * i
                drawCircle(
                    color = MintLight.copy(alpha = 0.25f),
                    radius = r,
                    center = center,
                    style = Stroke(width = 1.2.dp.toPx())
                )
            }

            // Crosshairs
            drawLine(
                color = MintLight.copy(alpha = 0.2f),
                start = Offset(center.x - maxRadius, center.y),
                end = Offset(center.x + maxRadius, center.y),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = MintLight.copy(alpha = 0.2f),
                start = Offset(center.x, center.y - maxRadius),
                end = Offset(center.x, center.y + maxRadius),
                strokeWidth = 1.dp.toPx()
            )

            // Radar Sweep Beam
            val rad = Math.toRadians(angle.toDouble())
            val sweepEnd = Offset(
                x = center.x + (maxRadius * cos(rad)).toFloat(),
                y = center.y + (maxRadius * sin(rad)).toFloat()
            )
            drawLine(
                brush = Brush.radialGradient(
                    colors = listOf(MintLight.copy(alpha = 0.9f), Color.Transparent),
                    center = center,
                    radius = maxRadius
                ),
                start = center,
                end = sweepEnd,
                strokeWidth = 3.dp.toPx()
            )

            // Hotspot 1 (Critical Late Blight)
            val p1 = Offset(center.x + maxRadius * 0.45f, center.y - maxRadius * 0.35f)
            drawCircle(
                color = CriticalRed.copy(alpha = 0.35f),
                radius = 24.dp.toPx() * pulse,
                center = p1
            )
            drawCircle(
                color = CriticalRed,
                radius = 7.dp.toPx(),
                center = p1
            )

            // Hotspot 2 (High Armyworm)
            val p2 = Offset(center.x - maxRadius * 0.5f, center.y - maxRadius * 0.2f)
            drawCircle(
                color = WarningOrange.copy(alpha = 0.3f),
                radius = 18.dp.toPx() * pulse,
                center = p2
            )
            drawCircle(
                color = WarningOrange,
                radius = 6.dp.toPx(),
                center = p2
            )

            // Hotspot 3 (Rice Blast)
            val p3 = Offset(center.x - maxRadius * 0.25f, center.y + maxRadius * 0.45f)
            drawCircle(
                color = EarthAmber.copy(alpha = 0.3f),
                radius = 15.dp.toPx(),
                center = p3
            )
            drawCircle(
                color = EarthAmber,
                radius = 5.dp.toPx(),
                center = p3
            )

            // Farmer GPS center point
            drawCircle(
                color = MintLight,
                radius = 5.dp.toPx(),
                center = center
            )
        }

        // Overlay Radar Info Badges
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(MintLight, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE SURVEILLANCE RADAR",
                        color = MintLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Surface(
                color = CriticalRed.copy(alpha = 0.85f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "$criticalCount Severe Outbreaks",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // Bottom radius labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Range: 5.0 km", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
            Text("Active Hotspots: $hotspotCount", color = MintLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LanguageSelectorDialog(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedLang by remember(currentLanguage) { mutableStateOf(currentLanguage) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("dialog_language_selector"),
        shape = RoundedCornerShape(24.dp),
        containerColor = MinimalSurface,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = CleanGreenLime,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = null,
                            tint = CleanGreenDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    text = AppStrings.selectAdvisoryLanguage(selectedLang),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MinimalTextPrimary
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                AppLanguage.values().forEach { lang ->
                    val isSelected = (lang == selectedLang)
                    Surface(
                        color = if (isSelected) CleanGreenLime.copy(alpha = 0.5f) else MinimalBackground,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) CleanGreenPrimary else MinimalBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedLang = lang
                                onLanguageSelected(lang)
                            }
                            .testTag("btn_language_${lang.code}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    selectedLang = lang
                                    onLanguageSelected(lang)
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = CleanGreenPrimary,
                                    unselectedColor = MinimalTextMuted
                                ),
                                modifier = Modifier.minimumInteractiveComponentSize()
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = lang.nativeName,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    color = if (isSelected) CleanGreenDark else MinimalTextPrimary
                                )
                                Text(
                                    text = lang.displayName,
                                    fontSize = 12.sp,
                                    color = if (isSelected) CleanGreenDark.copy(alpha = 0.8f) else MinimalTextSecondary
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = CleanGreenPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onLanguageSelected(selectedLang)
                    onDismiss()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CleanGreenPrimary,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag("btn_language_done")
            ) {
                Text(
                    text = AppStrings.done(selectedLang),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MinimalBorder),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MinimalTextSecondary
                ),
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag("btn_language_cancel")
            ) {
                Text(
                    text = AppStrings.cancel(selectedLang),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    )
}
