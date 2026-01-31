package com.resumearchitect.ui.screens.builder.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.resumearchitect.data.models.EmploymentType
import com.resumearchitect.data.models.WorkExperience
import com.resumearchitect.ui.components.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkExperienceDialog(
    experience: WorkExperience?,
    onDismiss: () -> Unit,
    onSave: (WorkExperience) -> Unit
) {
    // Form state
    var company by remember { mutableStateOf(experience?.company ?: "") }
    var position by remember { mutableStateOf(experience?.position ?: "") }
    var location by remember { mutableStateOf(experience?.location ?: "") }
    var employmentType by remember { mutableStateOf(experience?.employmentType ?: EmploymentType.FULL_TIME) }
    var isCurrentPosition by remember { mutableStateOf(experience?.isCurrentPosition ?: false) }
    var startDate by remember { mutableStateOf(experience?.startDate ?: System.currentTimeMillis()) }
    var endDate by remember { mutableStateOf(experience?.endDate) }
    var achievements by remember { mutableStateOf(experience?.achievements?.toMutableList() ?: mutableListOf()) }
    var currentAchievement by remember { mutableStateOf("") }
    
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showEmploymentTypeMenu by remember { mutableStateOf(false) }
    
    // Validation
    val isValid = company.isNotBlank() && position.isNotBlank()
    
    Dialog(onDismissRequest = onDismiss) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (experience == null) "Add Experience" else "Edit Experience",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    GlassIconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Scrollable content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Company
                    GlassTextField(
                        value = company,
                        onValueChange = { company = it },
                        label = "Company *",
                        placeholder = "Google, Microsoft, etc.",
                        leadingIcon = {
                            Icon(Icons.Default.Business, contentDescription = null)
                        }
                    )
                    
                    // Position
                    GlassTextField(
                        value = position,
                        onValueChange = { position = it },
                        label = "Position *",
                        placeholder = "Software Engineer, Product Manager, etc.",
                        leadingIcon = {
                            Icon(Icons.Default.Work, contentDescription = null)
                        }
                    )
                    
                    // Location
                    GlassTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = "Location",
                        placeholder = "San Francisco, CA",
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = null)
                        }
                    )
                    
                    // Employment Type
                    Box {
                        GlassTextField(
                            value = employmentType.name.replace('_', ' '),
                            onValueChange = { },
                            label = "Employment Type",
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showEmploymentTypeMenu = true }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        DropdownMenu(
                            expanded = showEmploymentTypeMenu,
                            onDismissRequest = { showEmploymentTypeMenu = false }
                        ) {
                            EmploymentType.values().forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.name.replace('_', ' ')) },
                                    onClick = {
                                        employmentType = type
                                        showEmploymentTypeMenu = false
                                    }
                                )
                            }
                        }
                    }
                    
                    // Dates
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Start Date
                        GlassTextField(
                            value = formatDate(startDate),
                            onValueChange = { },
                            label = "Start Date",
                            readOnly = true,
                            modifier = Modifier.weight(1f),
                            trailingIcon = {
                                IconButton(onClick = { showStartDatePicker = true }) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = null)
                                }
                            }
                        )
                        
                        // End Date
                        GlassTextField(
                            value = if (isCurrentPosition) "Present" else formatDate(endDate),
                            onValueChange = { },
                            label = "End Date",
                            readOnly = true,
                            enabled = !isCurrentPosition,
                            modifier = Modifier.weight(1f),
                            trailingIcon = {
                                if (!isCurrentPosition) {
                                    IconButton(onClick = { showEndDatePicker = true }) {
                                        Icon(Icons.Default.CalendarToday, contentDescription = null)
                                    }
                                }
                            }
                        )
                    }
                    
                    // Current Position Checkbox
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = isCurrentPosition,
                            onCheckedChange = { isCurrentPosition = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("I currently work here")
                    }
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Achievements Section
                    Text(
                        text = "Key Achievements",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Text(
                        text = "Add bullet points highlighting your accomplishments",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    
                    // Achievement input
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GlassTextField(
                            value = currentAchievement,
                            onValueChange = { currentAchievement = it },
                            placeholder = "Led a team of 5 engineers...",
                            modifier = Modifier.weight(1f),
                            singleLine = false,
                            maxLines = 3
                        )
                        
                        GlassButton(
                            onClick = {
                                if (currentAchievement.isNotBlank()) {
                                    achievements.add(currentAchievement)
                                    currentAchievement = ""
                                }
                            },
                            variant = GlassButtonVariant.PRIMARY,
                            enabled = currentAchievement.isNotBlank()
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    
                    // Achievement list
                    if (achievements.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        achievements.forEachIndexed { index, achievement ->
                            AchievementItem(
                                achievement = achievement,
                                onDelete = { achievements.removeAt(index) }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlassButton(
                        onClick = onDismiss,
                        variant = GlassButtonVariant.OUTLINED,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                    
                    GlassButton(
                        onClick = {
                            val newExperience = WorkExperience(
                                id = experience?.id ?: UUID.randomUUID().toString(),
                                resumeId = experience?.resumeId ?: "",
                                company = company,
                                position = position,
                                location = location,
                                employmentType = employmentType,
                                startDate = startDate,
                                endDate = if (isCurrentPosition) null else endDate,
                                isCurrentPosition = isCurrentPosition,
                                achievements = achievements,
                                displayOrder = experience?.displayOrder ?: 0
                            )
                            onSave(newExperience)
                        },
                        variant = GlassButtonVariant.PRIMARY,
                        enabled = isValid,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
    
    // Date picker dialogs
    if (showStartDatePicker) {
        com.resumearchitect.ui.components.MonthYearPickerDialog(
            initialDate = startDate,
            onDismiss = { showStartDatePicker = false },
            onDateSelected = { 
                startDate = it
                showStartDatePicker = false
            }
        )
    }
    
    if (showEndDatePicker) {
        com.resumearchitect.ui.components.MonthYearPickerDialog(
            initialDate = endDate ?: System.currentTimeMillis(),
            onDismiss = { showEndDatePicker = false },
            onDateSelected = { 
                endDate = it
                showEndDatePicker = false
            }
        )
    }
}

@Composable
fun AchievementItem(
    achievement: String,
    onDelete: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = achievement,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            GlassIconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun formatDate(timestamp: Long?): String {
    if (timestamp == null) return ""
    val sdf = SimpleDateFormat("MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
