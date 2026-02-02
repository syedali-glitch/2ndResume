package com.resumearchitect.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.resumearchitect.pdf.TemplateConfig
import com.resumearchitect.templates.TemplateCategory

/**
 * Enhanced template preview card with icon, category, and description
 */
@Composable
fun TemplatePreviewCard(
    template: TemplateConfig,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = rememberHapticFeedback()
    
    // Get icon and gradient based on category
    val (icon, gradient) = remember(template.category) {
        when (template.category) {
            TemplateCategory.MODERN -> Icons.Default.AutoAwesome to listOf(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
            )
            TemplateCategory.CREATIVE -> Icons.Default.Palette to listOf(
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f),
                MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
            )
            TemplateCategory.EXECUTIVE -> Icons.Default.BusinessCenter to listOf(
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)
            )
            TemplateCategory.TECHNICAL -> Icons.Default.Code to listOf(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)
            )
            TemplateCategory.ALL -> Icons.Default.Description to listOf(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
        }
    }
    
    GlassCardClickable(
        onClick = {
            haptic.click()
            onClick()
        },
        modifier = modifier,
        cornerRadius = 16.dp,
        frostedBackground = if (isSelected) 
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        else 
            MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with icon and category badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Template icon with gradient background
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.linearGradient(gradient)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                // Category badge
                if (template.category != TemplateCategory.ALL) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = template.category.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Template name
            Text(
                text = template.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Template description
            Text(
                text = getTemplateDescription(template),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 2
            )
            
            // Selection indicator
            if (isSelected) {
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Selected",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * Get descriptive text for each template
 */
private fun getTemplateDescription(template: TemplateConfig): String {
    return when (template.name) {
        "Clarity" -> "Clean and modern design with clear sections"
        "Minimal Pro" -> "Minimalist layout focusing on content"
        "Metro" -> "Contemporary style with bold typography"
        "Artisan" -> "Creative layout with artistic flair"
        "Canvas" -> "Flexible design for creative professionals"
        "Executive Suite" -> "Premium layout for senior roles"
        "Boardroom" -> "Professional design for executives"
        "CodeMaster" -> "Developer-focused with technical sections"
        "DevPro" -> "Technical resume for software engineers"
        "Terminal" -> "Tech-inspired monospace design"
        else -> "Professional resume template"
    }
}
