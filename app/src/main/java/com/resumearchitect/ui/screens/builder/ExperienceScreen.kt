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
import com.resumearchitect.data.models.WorkExperience
import com.resumearchitect.data.models.EmploymentType
import com.resumearchitect.ui.components.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExperienceScreen(viewModel: BuilderViewModel) {
    val experiences by viewModel.workExperiences.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingExperience by remember { mutableStateOf<WorkExperience?>(null) }
    var experienceToDelete by remember { mutableStateOf<WorkExperience?>(null) }
    
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
                    text = "Work Experience",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = "${experiences.size} ${if (experiences.size == 1) "position" else "positions"}",
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
                Text("Add Experience")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (experiences.isEmpty()) {
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
                        Icons.Default.Work,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "No work experience yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    
                    Text(
                        text = "Add your professional experience to showcase your career",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }
        } else {
            // Experience list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(experiences, key = { it.id }) { experience ->
                    ExperienceCard(
                        experience = experience,
                        onEdit = { editingExperience = experience },
                        onDelete = { experienceToDelete = experience }
                    )
                }
                
                // Bottom spacer
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
    
    // Delete confirmation
    experienceToDelete?.let { experience ->
        com.resumearchitect.ui.components.ConfirmationDialog(
            title = "Delete Experience?",
            message = "Are you sure you want to delete your experience at ${experience.company}?",
            confirmText = "Delete",
            onConfirm = {
                viewModel.deleteWorkExperience(experience)
                experienceToDelete = null
            },
            onDismiss = { experienceToDelete = null }
        )
    }
    
    // Add/Edit Dialog
    if (showAddDialog || editingExperience != null) {
        com.resumearchitect.ui.screens.builder.dialogs.WorkExperienceDialog(
            experience = editingExperience,
            onDismiss = {
                showAddDialog = false
                editingExperience = null
            },
            onSave = { experience ->
                if (editingExperience != null) {
                    viewModel.updateWorkExperience(experience)
                } else {
                    viewModel.addWorkExperience(experience)
                }
                showAddDialog = false
                editingExperience = null
            }
        )
    }
}

@Composable
fun ExperienceCard(
    experience: WorkExperience,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    
    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = experience.position,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = experience.company,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (experience.location.isNotBlank()) {
                            Chip(text = experience.location, icon = Icons.Default.LocationOn)
                        }
                        
                        Chip(
                            text = if (experience.isCurrentPosition) "Present" 
                                   else formatDate(experience.endDate),
                            icon = Icons.Default.CalendarToday
                        )
                    }
                }
                
                Box {
                    GlassIconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Options")
                    }
                    
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                onEdit()
                                showMenu = false
                            },
                            leadingIcon = { Icon(Icons.Default.Edit, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                onDelete()
                                showMenu = false
                            },
                            leadingIcon = { Icon(Icons.Default.Delete, null) }
                        )
                    }
                }
            }
            
            if (experience.achievements.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    experience.achievements.take(3).forEach { achievement ->
                        Row {
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = achievement,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                    
                    if (experience.achievements.size > 3) {
                        Text(
                            text = "+${experience.achievements.size - 3} more",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Chip(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.height(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun ExperienceDialog(
    experience: WorkExperience?,
    onDismiss: () -> Unit,
    onSave: (WorkExperience) -> Unit
) {
    // Dialog implementation - simplified placeholder
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (experience == null) "Add Experience" else "Edit Experience") },
        text = {
            Text("Full experience editor with fields for company, position, dates, and achievements coming soon...")
        },
        confirmButton = {
            GlassButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

private fun formatDate(timestamp: Long?): String {
    if (timestamp == null) return ""
    val sdf = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
