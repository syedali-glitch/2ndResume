package com.resumearchitect.ui.screens.builder.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.resumearchitect.data.models.Education
import com.resumearchitect.ui.components.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationDialog(
    education: Education?,
    onDismiss: () -> Unit,
    onSave: (Education) -> Unit
) {
    // Form state
    var institution by remember { mutableStateOf(education?.institution ?: "") }
    var degree by remember { mutableStateOf(education?.degree ?: "") }
    var fieldOfStudy by remember { mutableStateOf(education?.fieldOfStudy ?: "") }
    var gpa by remember { mutableStateOf(education?.gpa?.toString() ?: "") }
    var startDate by remember { mutableStateOf(education?.startDate ?: System.currentTimeMillis()) }
    var endDate by remember { mutableStateOf(education?.endDate ?: System.currentTimeMillis()) }
    var honors by remember { mutableStateOf(education?.honors?.joinToString("\n") ?: "") }
    var relevantCoursework by remember { mutableStateOf(education?.relevantCoursework?.joinToString(", ") ?: "") }
    
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    
    // Validation
    val isValid = institution.isNotBlank() && degree.isNotBlank() && fieldOfStudy.isNotBlank()
    
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
                        text = if (education == null) "Add Education" else "Edit Education",
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
                    // Institution
                    GlassTextField(
                        value = institution,
                        onValueChange = { institution = it },
                        label = "Institution *",
                        placeholder = "Stanford University, MIT, etc.",
                        leadingIcon = {
                            Icon(Icons.Default.School, contentDescription = null)
                        }
                    )
                    
                    // Degree
                    GlassTextField(
                        value = degree,
                        onValueChange = { degree = it },
                        label = "Degree *",
                        placeholder = "Bachelor of Science, Master's, etc.",
                        leadingIcon = {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null)
                        }
                    )
                    
                    // Field of Study
                    GlassTextField(
                        value = fieldOfStudy,
                        onValueChange = { fieldOfStudy = it },
                        label = "Field of Study *",
                        placeholder = "Computer Science, Business, etc.",
                        leadingIcon = {
                            Icon(Icons.Default.MenuBook, contentDescription = null)
                        }
                    )
                    
                    // GPA
                    GlassTextField(
                        value = gpa,
                        onValueChange = { gpa = it },
                        label = "GPA (Optional)",
                        placeholder = "3.8",
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        leadingIcon = {
                            Icon(Icons.Default.Grade, contentDescription = null)
                        }
                    )
                    
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
                            modifier = Modifier.weight(1f),
                            trailingIcon = {
                                IconButton(onClick = { showStartDatePicker = true }) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = null)
                                }
                            }
                        )
                        
                        // End Date
                        GlassTextField(
                            value = formatDate(endDate),
                            onValueChange = { },
                            label = "End Date",
                            modifier = Modifier.weight(1f),
                            trailingIcon = {
                                IconButton(onClick = { showEndDatePicker = true }) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = null)
                                }
                            }
                        )
                    }
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Honors & Awards
                    Text(
                        text = "Honors & Awards (Optional)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    GlassTextField(
                        value = honors,
                        onValueChange = { honors = it },
                        placeholder = "Dean's List, Summa Cum Laude\nPresidential Scholar",
                        singleLine = false,
                        maxLines = 4,
                        minLines = 2
                    )
                    
                    Text(
                        text = "Enter each honor on a new line",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    
                    // Relevant Coursework
                    Text(
                        text = "Relevant Coursework (Optional)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    GlassTextField(
                        value = relevantCoursework,
                        onValueChange = { relevantCoursework = it },
                        placeholder = "Data Structures, Machine Learning, Algorithms",
                        singleLine = false,
                        maxLines = 3
                    )
                    
                    Text(
                        text = "Separate courses with commas",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
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
                            val newEducation = Education(
                                id = education?.id ?: UUID.randomUUID().toString(),
                                resumeId = education?.resumeId ?: "",
                                institution = institution,
                                degree = degree,
                                fieldOfStudy = fieldOfStudy,
                                gpa = if (gpa.isBlank()) null else gpa,
                                startDate = startDate,
                                endDate = endDate,
                                honors = if (honors.isBlank()) emptyList() 
                                         else honors.split("\n").map { it.trim() }.filter { it.isNotBlank() },
                                relevantCoursework = if (relevantCoursework.isBlank()) emptyList()
                                                     else relevantCoursework.split(",").map { it.trim() }.filter { it.isNotBlank() },
                                displayOrder = education?.displayOrder ?: 0
                            )
                            onSave(newEducation)
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
            initialDate = endDate,
            onDismiss = { showEndDatePicker = false },
            onDateSelected = { 
                endDate = it
                showEndDatePicker = false
            }
        )
    }
}

private fun formatDate(timestamp: Long?): String {
    if (timestamp == null) return ""
    val sdf = SimpleDateFormat("MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
