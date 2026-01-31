package com.resumearchitect.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    frostedBackground: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
    borderColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    elevation: Dp = 4.dp,
    cornerRadius: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        frostedBackground.copy(alpha = 0.8f),
                        frostedBackground.copy(alpha = 0.6f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
        )
        
        Box(
            modifier = Modifier.padding(elevation / 2),
            content = content
        )
    }
}

@Composable
fun GlassCardClickable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    frostedBackground: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
    borderColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    elevation: Dp = 4.dp,
    cornerRadius: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.clip(RoundedCornerShape(cornerRadius)),
        color = Color.Transparent,
        shape = RoundedCornerShape(cornerRadius)
    ) {
        GlassCard(
            frostedBackground = frostedBackground,
            borderColor = borderColor,
            elevation = elevation,
            cornerRadius = cornerRadius,
            content = content
        )
    }
}
