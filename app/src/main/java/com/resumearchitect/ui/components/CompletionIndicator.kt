package com.resumearchitect.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Circular completion indicator showing resume progress
 */
@Composable
fun CompletionIndicator(
    completionPercentage: Int,
    modifier: Modifier = Modifier,
    size: Int = 48
) {
    val color = when {
        completionPercentage >= 80 -> Color(0xFF10B981) // Green
        completionPercentage >= 50 -> Color(0xFFF59E0B) // Yellow
        else -> Color(0xFFEF4444) // Red
    }
    
    Box(
        modifier = modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = completionPercentage / 100f,
            modifier = Modifier.fillMaxSize(),
            color = color,
            strokeWidth = 4.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
        
        Text(
            text = "$completionPercentage%",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

/**
 * Badge showing completion status
 */
@Composable
fun CompletionBadge(
    completionPercentage: Int,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, label) = when {
        completionPercentage >= 80 -> Triple(
            Color(0xFF10B981),
            Color.White,
            "Complete"
        )
        completionPercentage >= 50 -> Triple(
            Color(0xFFF59E0B),
            Color.White,
            "In Progress"
        )
        else -> Triple(
            Color(0xFFEF4444),
            Color.White,
            "Incomplete"
        )
    }
    
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = backgroundColor
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
