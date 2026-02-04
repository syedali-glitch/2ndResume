package com.resumearchitect.ui.screens.templates

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.resumearchitect.templates.ColorSchemes
import com.resumearchitect.templates.TemplateRegistry
import com.resumearchitect.ui.components.*

/**
 * Theme Preview Screen with sample resume data and color selection
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemePreviewScreen(
    resumeId: String,
    navController: NavController,
    isNewResume: Boolean = false,
    onApplyTheme: (templateId: String, colorSchemeId: String) -> Unit = { _, _ -> }
) {
    var selectedTemplateId by remember { mutableStateOf("clarity") }
    var selectedColorSchemeId by remember { mutableStateOf("professional-blue") }
    
    val templates = remember { TemplateRegistry.getAllTemplates() }
    val colorSchemes = remember { ColorSchemes.getAllSchemes() }
    
    val selectedTemplate = templates.find { it.id == selectedTemplateId } ?: templates.first()
    val selectedColorScheme = colorSchemes.find { it.id == selectedColorSchemeId } ?: colorSchemes.first()
    
    Scaffold(
        topBar = {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                cornerRadius = 16.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Theme Preview",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Choose template & colors",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    
                    // Apply Button
                    GlassButton(
                        onClick = {
                            onApplyTheme(selectedTemplateId, selectedColorSchemeId)
                            navController.navigateUp()
                        },
                        variant = GlassButtonVariant.PRIMARY
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isNewResume) "Create Resume" else "Apply")
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Live Preview Section
            item {
                Text(
                    text = "Live Preview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                SampleResumePreview(
                    templateId = selectedTemplateId,
                    colorScheme = selectedColorScheme,
                    sampleData = ThemePreviewLayouts.PreviewData(
                        name = "John Anderson",
                        role = "Senior Software Engineer",
                        email = "john.anderson@email.com",
                        phone = "(555) 123-4567",
                        summary = "Experienced software engineer with 8+ years of expertise in mobile and web development. Passionate about creating scalable applications.",
                        company = "Tech Solutions Inc.",
                        dates = "2020 - Present",
                        description = "Led development of flagship mobile app. Improved performance by 40%.",
                        university = "Stanford University",
                        degree = "Master's in Computer Science",
                        skills = listOf("Kotlin", "Android", "Compose", "System Design")
                    )
                )
            }
            
            // Template Selection
            item {
                Text(
                    text = "Templates",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(templates) { template ->
                        TemplatePreviewCard(
                            templateName = template.name,
                            templateDescription = template.description,
                            isSelected = template.id == selectedTemplateId,
                            onClick = { selectedTemplateId = template.id }
                        )
                    }
                }
            }
            
            // Color Scheme Selection
            item {
                Text(
                    text = "Color Schemes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                ColorSchemeGrid(
                    colorSchemes = colorSchemes,
                    selectedId = selectedColorSchemeId,
                    onSelect = { selectedColorSchemeId = it }
                )
            }
        }
    }
}

/**
 * Sample resume preview with DYNAMIC layouts
 */
@Composable
fun SampleResumePreview(
    templateId: String,
    colorScheme: ColorSchemes.ColorSchemeConfig,
    sampleData: ThemePreviewLayouts.PreviewData
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp), // Taller for better preview
        cornerRadius = 16.dp
    ) {
            ) {
                Column {
                    Text(
                        text = "John Anderson",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Senior Software Engineer",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Text(
                        text = "john.anderson@email.com • (555) 123-4567",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
            
            // Content Area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                // Summary
                SampleSection(
                    title = "Professional Summary",
                    accentColor = Color(colorScheme.accentColor)
                ) {
                    Text(
                        text = "Experienced software engineer with 8+ years of expertise in mobile and web development. " +
                                "Passionate about creating scalable applications and leading high-performance teams.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Experience
                SampleSection(
                    title = "Work Experience",
                    accentColor = Color(colorScheme.accentColor)
                ) {
                    Column {
                        Text(
                            text = "Tech Solutions Inc.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Text(
                            text = "Senior Engineer • 2020 - Present",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            text = "• Led development of flagship mobile app\n• Improved performance by 40%",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray,
                            maxLines = 2
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Skills
                SampleSection(
                    title = "Skills",
                    accentColor = Color(colorScheme.accentColor)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Kotlin", "Android", "iOS", "React").forEach { skill ->
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(colorScheme.accentColor).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = skill,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(colorScheme.accentColor)
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Template name overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
            ) {
                Text(
                    text = templateName,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun SampleSection(
    title: String,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(16.dp)
                    .background(accentColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = accentColor,
                letterSpacing = 1.sp
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        content()
    }
}

/**
 * Template preview card for horizontal selection
 */
@Composable
fun TemplatePreviewCard(
    templateName: String,
    templateDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }
    
    GlassCard(
        modifier = Modifier
            .width(140.dp)
            .height(180.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        cornerRadius = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mini preview placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Description,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = templateName,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * Color scheme grid for selection
 */
@Composable
fun ColorSchemeGrid(
    colorSchemes: List<ColorSchemes.ColorSchemeConfig>,
    selectedId: String,
    onSelect: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        colorSchemes.chunked(4).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { scheme ->
                    ColorSchemeChip(
                        scheme = scheme,
                        isSelected = scheme.id == selectedId,
                        onClick = { onSelect(scheme.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill remaining space if row is incomplete
                repeat(4 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ColorSchemeChip(
    scheme: ColorSchemes.ColorSchemeConfig,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }
    
    GlassCard(
        modifier = modifier
            .height(80.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        cornerRadius = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Color circles
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(scheme.primaryColor))
                        .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(scheme.accentColor))
                        .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                )
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Text(
                text = scheme.name,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Selected",
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
