package com.resumearchitect.ui.screens.builder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.resumearchitect.data.models.Skill
import com.resumearchitect.data.models.SkillCategory
import com.resumearchitect.ui.components.*

@Composable
fun SkillsScreen(viewModel: BuilderViewModel) {
    val skills by viewModel.skills.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<SkillCategory?>(null) }
    
    // Group skills by category
    val groupedSkills = skills.groupBy { it.category }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Skills",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = "${skills.size} skills across ${groupedSkills.size} categories",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            GlassButton(
                onClick = { showAddDialog = true },
                variant = GlassButtonVariant.PRIMARY
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Skill")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (skills.isEmpty()) {
            // Empty state
            GlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "No skills added yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    
                    Text(
                        text = "Showcase your expertise and abilities",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }
        } else {
            // Skills by category
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                groupedSkills.forEach { (category, categorySkills) ->
                    item(key = category) {
                        SkillCategoryCard(
                            category = category,
                            skills = categorySkills,
                            onDeleteSkill = { skill -> viewModel.deleteSkill(skill) }
                        )
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
    
    if (showAddDialog) {
        AddSkillDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { skillName, category ->
                viewModel.addSkill(
                    Skill(
                        resumeId = "", // Will be set by ViewModel
                        name = skillName,
                        category = category
                    )
                )
                showAddDialog = false
            }
        )
    }
}

@Composable
fun SkillCategoryCard(
    category: SkillCategory,
    skills: List<Skill>,
    onDeleteSkill: (Skill) -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    getCategoryIcon(category),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                
                Text(
                    text = category.name.replace('_', ' '),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "(${skills.size})",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Skill chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                skills.forEach { skill ->
                    SkillChip(
                        skill = skill,
                        onDelete = { onDeleteSkill(skill) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable androidx.compose.foundation.layout.FlowRowScope.() -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        content = content
    )
}

@Composable
fun SkillChip(
    skill: Skill,
    onDelete: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = skill.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Remove",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun AddSkillDialog(
    onDismiss: () -> Unit,
    onAdd: (String, SkillCategory) -> Unit
) {
    var skillName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(SkillCategory.TECHNICAL) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Skill") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                GlassTextField(
                    value = skillName,
                    onValueChange = { skillName = it },
                    label = "Skill Name",
                    placeholder = "e.g., Kotlin, Leadership, Spanish"
                )
                
                // Category selector (simplified)
                Text("Category: ${selectedCategory.name.replace('_', ' ')}")
            }
        },
        confirmButton = {
            GlassButton(
                onClick = {
                    if (skillName.isNotBlank()) {
                        onAdd(skillName, selectedCategory)
                    }
                },
                enabled = skillName.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            GlassButton(
                onClick = onDismiss,
                variant = GlassButtonVariant.OUTLINED
            ) {
                Text("Cancel")
            }
        }
    )
}

private fun getCategoryIcon(category: SkillCategory): androidx.compose.ui.graphics.vector.ImageVector {
    return when (category) {
        SkillCategory.TECHNICAL -> Icons.Default.Code
        SkillCategory.SOFT_SKILLS -> Icons.Default.People
        SkillCategory.LANGUAGES -> Icons.Default.Language
        SkillCategory.TOOLS -> Icons.Default.Build
        SkillCategory.CERTIFICATIONS -> Icons.Default.Verified
        SkillCategory.CUSTOM -> Icons.Default.Star
    }
}
