package com.resumearchitect.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = GlassPrimaryLight,
    onPrimary = GlassOnPrimaryLight,
    primaryContainer = GlassPrimaryContainerLight,
    onPrimaryContainer = GlassOnPrimaryContainerLight,
    secondary = GlassSecondaryLight,
    onSecondary = GlassOnSecondaryLight,
    secondaryContainer = GlassSecondaryContainerLight,
    onSecondaryContainer = GlassOnSecondaryContainerLight,
    tertiary = GlassTertiaryLight,
    onTertiary = GlassOnTertiaryLight,
    tertiaryContainer = GlassTertiaryContainerLight,
    onTertiaryContainer = GlassOnTertiaryContainerLight,
    error = GlassError,
    onError = GlassOnError,
    errorContainer = GlassErrorContainer,
    onErrorContainer = GlassOnErrorContainer,
    background = GlassBackgroundLight,
    onBackground = GlassOnBackgroundLight,
    surface = GlassSurfaceLight,
    onSurface = GlassOnSurfaceLight,
    surfaceVariant = GlassSurfaceVariantLight,
    onSurfaceVariant = GlassOnSurfaceVariantLight,
)

private val DarkColorScheme = darkColorScheme(
    primary = GlassPrimaryDark,
    onPrimary = GlassOnPrimaryDark,
    primaryContainer = GlassPrimaryContainerDark,
    onPrimaryContainer = GlassOnPrimaryContainerDark,
    secondary = GlassSecondaryDark,
    onSecondary = GlassOnSecondaryDark,
    secondaryContainer = GlassSecondaryContainerDark,
    onSecondaryContainer = GlassOnSecondaryContainerDark,
    tertiary = GlassTertiaryDark,
    onTertiary = GlassOnTertiaryDark,
    tertiaryContainer = GlassTertiaryContainerDark,
    onTertiaryContainer = GlassOnTertiaryContainerDark,
    error = GlassError,
    onError = GlassOnError,
    errorContainer = GlassErrorContainer,
    onErrorContainer = GlassOnErrorContainer,
    background = GlassBackgroundDark,
    onBackground = GlassOnBackgroundDark,
    surface = GlassSurfaceDark,
    onSurface = GlassOnSurfaceDark,
    surfaceVariant = GlassSurfaceVariantDark,
    onSurfaceVariant = GlassOnSurfaceVariantDark,
)

@Composable
fun ResumeArchitectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled for consistent glassmorphism branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
