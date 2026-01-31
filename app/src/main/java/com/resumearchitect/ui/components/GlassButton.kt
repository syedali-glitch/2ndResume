package com.resumearchitect.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class GlassButtonVariant {
    PRIMARY,
    SECONDARY,
    TERTIARY,
    OUTLINED
}

@Composable
fun GlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: GlassButtonVariant = GlassButtonVariant.PRIMARY,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    cornerRadius: Dp = 12.dp,
    hapticFeedback: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val view = LocalView.current
    
    val (backgroundBrush, contentColor, borderColor) = when (variant) {
        GlassButtonVariant.PRIMARY -> Triple(
            Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                )
            ),
            MaterialTheme.colorScheme.onPrimary,
            Color.Transparent
        )
        GlassButtonVariant.SECONDARY -> Triple(
            Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                )
            ),
            MaterialTheme.colorScheme.onSecondary,
            MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.3f)
        )
        GlassButtonVariant.TERTIARY -> Triple(
            Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                )
            ),
            MaterialTheme.colorScheme.onSurface,
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        )
        GlassButtonVariant.OUTLINED -> Triple(
            Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.Transparent
                )
            ),
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
    }
    
    val finalModifier = if (variant == GlassButtonVariant.OUTLINED) {
        modifier
            .clip(RoundedCornerShape(cornerRadius))
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
    } else {
        modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundBrush)
            .then(
                if (borderColor != Color.Transparent) {
                    Modifier.border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(cornerRadius)
                    )
                } else Modifier
            )
    }
    
    Surface(
        onClick = {
            if (hapticFeedback) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }
            onClick()
        },
        modifier = finalModifier,
        enabled = enabled && !isLoading,
        color = Color.Transparent,
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = contentColor,
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}

@Composable
fun GlassIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    hapticFeedback: Boolean = true,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    
    Surface(
        onClick = {
            if (hapticFeedback) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }
            onClick()
        },
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            ),
        enabled = enabled,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}
