package com.resumearchitect.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

/**
 * Confirmation dialog for destructive actions
 */
@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String = "Delete",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isDestructive: Boolean = true
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            if (isDestructive) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            GlassButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
                variant = if (isDestructive) GlassButtonVariant.PRIMARY else GlassButtonVariant.PRIMARY
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        }
    )
}
