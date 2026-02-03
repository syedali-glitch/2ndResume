package com.resumearchitect.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.resumearchitect.templates.ColorSchemes
import com.resumearchitect.templates.TemplateColorScheme

/**
 * Color scheme selector with 8 color options
 * Shows swatches in a grid and allows selection
 */
@Composable
fun ColorSchemeSelector(
    selectedScheme: TemplateColorScheme?,
    onSchemeSelected: (TemplateColorScheme) -> Unit,
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
                text = "Choose Color Scheme",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "Select a color palette for your resume",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Color scheme grid (4 columns, 2 rows)
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(160.dp)
            ) {
                items(ColorSchemes.ALL) { scheme ->
                    ColorSchemeSwatch(
                        scheme = scheme,
                        isSelected = selectedScheme?.id == scheme.id,
                        onClick = {
                            haptic.selection()
                            onSchemeSelected(scheme)
                        }
                    )
                }
            }
            
            // Selected scheme name
            if (selectedScheme != null) {
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Selected: ${selectedScheme.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

/**
 * Individual color swatch with selection indicator
 */
@Composable
fun ColorSchemeSwatch(
    scheme: TemplateColorScheme,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
            )
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) scheme.primary else Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Color circles showing primary and accent
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Primary color circle
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(scheme.primary)
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                // Accent color circle
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(scheme.accent)
                )
            }
            
            // Check icon if selected
            if (isSelected) {
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = scheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
