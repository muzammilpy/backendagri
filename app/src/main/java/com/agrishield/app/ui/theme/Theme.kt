package com.agrishield.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// AgriShield colour palette — deep forest greens
val Emerald900 = Color(0xFF064E3B)
val Emerald700 = Color(0xFF047857)
val Emerald500 = Color(0xFF10B981)
val Emerald300 = Color(0xFF6EE7B7)
val Emerald100 = Color(0xFFD1FAE5)
val DarkBg     = Color(0xFF071E16)
val SurfaceCard = Color(0xFFF8FAFC)
val DangerRed  = Color(0xFFEF4444)
val WarnAmber  = Color(0xFFF59E0B)
val InfoBlue   = Color(0xFF3B82F6)

private val AgriColorScheme = darkColorScheme(
    primary          = Emerald500,
    onPrimary        = Color.White,
    primaryContainer = Emerald900,
    onPrimaryContainer = Emerald300,
    secondary        = Emerald300,
    onSecondary      = Emerald900,
    background       = DarkBg,
    onBackground     = Color.White,
    surface          = Color(0xFF0C2C20),
    onSurface        = Color.White,
    surfaceVariant   = Color(0xFF1A3A2A),
    error            = DangerRed,
)

@Composable
fun AgriShieldTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AgriColorScheme,
        typography  = Typography(),
        content     = content
    )
}

// Convenience severity colours
@Composable
fun severityColor(severity: com.agrishield.app.data.model.Severity) = when (severity) {
    com.agrishield.app.data.model.Severity.CRITICAL -> DangerRed
    com.agrishield.app.data.model.Severity.HIGH     -> WarnAmber
    com.agrishield.app.data.model.Severity.MODERATE -> Color(0xFFF59E0B)
    com.agrishield.app.data.model.Severity.LOW      -> Emerald500
}
