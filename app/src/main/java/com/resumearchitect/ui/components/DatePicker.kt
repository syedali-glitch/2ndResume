package com.resumearchitect.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.DialogProperties
import java.text.SimpleDateFormat
import java.util.*

/**
 * Date picker dialog for selecting month and year
 * Returns timestamp in milliseconds
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthYearPickerDialog(
    initialDate: Long? = null,
    onDismiss: () -> Unit,
    onDateSelected: (Long) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate ?: System.currentTimeMillis()
    )
    
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text("Select Date", modifier = androidx.compose.ui.Modifier.padding(16.dp))
            }
        )
    }
}

/**
 * Format timestamp to MM/yyyy
 */
fun formatMonthYear(timestamp: Long?): String {
    if (timestamp == null) return ""
    val sdf = SimpleDateFormat("MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

/**
 * Format timestamp to full date
 */
fun formatFullDate(timestamp: Long?): String {
    if (timestamp == null) return ""
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
