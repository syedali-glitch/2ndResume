package com.resumearchitect.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Quick Actions Panel for Homepage
 * Provides prominent CTAs for main user actions
 */
@Composable
fun QuickActionsPanel(
    onCreateNew: () -> Unit,
    onBrowseTemplates: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = rememberHapticFeedback()
    
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        cornerRadius = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Create New Resume Button
                QuickActionCard(
                    icon = Icons.Default.Add,
                    title = "Create New",
                    description = "Start fresh resume",
                    onClick = {
                        haptic.click()
                        onCreateNew()
                    },
                    modifier = Modifier.weight(1f),
                    isPrimary = true
                )
                
                // Browse Templates Button
                QuickActionCard(
                    icon = Icons.Default.Palette,
                    title = "Templates",
                    description = "Browse 10 designs",
                    onClick = {
                        haptic.click()
                        onBrowseTemplates()
                    },
                    modifier = Modifier.weight(1f),
                    isPrimary = false
                )
            }
        }
    }
}

@Composable
fun QuickActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false
) {
    GlassCardClickable(
        onClick = onClick,
        modifier = modifier,
        cornerRadius = 12.dp,
        frostedBackground = if (isPrimary) 
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        else 
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = if (isPrimary) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
