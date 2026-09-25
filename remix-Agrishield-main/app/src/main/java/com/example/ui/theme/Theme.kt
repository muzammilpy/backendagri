package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = CleanGreenDark,
    primaryContainer = CleanGreenPrimary,
    onPrimaryContainer = CleanGreenLight,
    secondary = DarkSecondary,
    onSecondary = CleanGreenDark,
    secondaryContainer = SoftWarningText,
    onSecondaryContainer = SoftWarningCard,
    tertiary = CleanGreenLime,
    onTertiary = CleanGreenDark,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = CleanGreenLight,
    outline = CleanGreenPrimary,
    error = CriticalRed,
    errorContainer = Color(0xFF5C1414),
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = CleanGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = CleanGreenLime,
    onPrimaryContainer = CleanGreenDark,
    secondary = SoftWarningText,
    onSecondary = Color.White,
    secondaryContainer = SoftWarningCard,
    onSecondaryContainer = SoftWarningTitle,
    tertiary = SoftPurpleText,
    onTertiary = Color.White,
    tertiaryContainer = SoftPurpleCard,
    onTertiaryContainer = SoftPurpleText,
    background = MinimalBackground,
    onBackground = MinimalTextPrimary,
    surface = MinimalSurface,
    onSurface = MinimalTextPrimary,
    surfaceVariant = MinimalSurfaceVariant,
    onSurfaceVariant = MinimalTextSecondary,
    outline = MinimalBorder,
    outlineVariant = MinimalBorderSoft,
    error = CriticalRed,
    errorContainer = SoftCriticalCard,
    onError = Color.White,
    onErrorContainer = SoftCriticalText
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use intentional themed colors
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

