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
import com.resumearchitect.data.models.Education
import com.resumearchitect.ui.components.*

@Composable
fun EducationScreen(viewModel: BuilderViewModel) {
    val educations by viewModel.educations.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingEducation by remember { mutableStateOf<Education?>(null) }
    var educationToDelete by remember { mutableStateOf<Education?>(null) }
    
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
                    text = "Education",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = "${educations.size} ${if (educations.size == 1) "degree" else "degrees"}",
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
                Text("Add Education")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (educations.isEmpty()) {
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
                        Icons.Default.School,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "No education added yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    
                    Text(
                        text = "Add your academic background and achievements",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }
        } else {
            // Education list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(educations, key = { it.id }) { education ->
                    EducationCard(
                        education = education,
                        onEdit = { editingEducation = education },
                        onDelete = { educationToDelete = education }
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
    
    // Delete confirmation
    educationToDelete?.let { education ->
        com.resumearchitect.ui.components.ConfirmationDialog(
            title = "Delete Education?",
            message = "Are you sure you want to delete ${education.degree} from ${education.institution}?",
            confirmText = "Delete",
            onConfirm = {
                viewModel.deleteEducation(education)
                educationToDelete = null
            },
            onDismiss = { educationToDelete = null }
        )
    }
    
    if (showAddDialog || editingEducation != null) {
        com.resumearchitect.ui.screens.builder.dialogs.EducationDialog(
            education = editingEducation,
            onDismiss = {
                showAddDialog = false
                editingEducation = null
            },
            onSave = { education ->
                if (editingEducation != null) {
                    viewModel.updateEducation(education)
                } else {
                    viewModel.addEducation(education)
                }
                showAddDialog = false
                editingEducation = null
            }
        )
    }
}

@Composable
fun EducationCard(
    education: Education,
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
                        text = "${education.degree} in ${education.fieldOfStudy}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = education.institution,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    if (education.gpa != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "GPA: ${education.gpa}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
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
        }
    }
}
