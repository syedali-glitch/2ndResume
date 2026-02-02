package com.resumearchitect.ui.components

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView

/**
 * Haptic feedback helper for satisfying tactile responses
 * Safe implementation with compatibility checks and error handling
 */
class HapticFeedback(private val view: android.view.View) {
    
    private fun performSafeHaptic(feedbackConstant: Int) {
        try {
            // Check if haptic feedback is enabled
            if (view.isHapticFeedbackEnabled) {
                view.performHapticFeedback(
                    feedbackConstant,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                )
            }
        } catch (e: Exception) {
            // Silently fail - haptic feedback is non-critical
            // Some devices may not support certain haptic types
        }
    }
    
    fun click() {
        performSafeHaptic(HapticFeedbackConstants.VIRTUAL_KEY)
    }
    
    fun longPress() {
        performSafeHaptic(HapticFeedbackConstants.LONG_PRESS)
    }
    
    fun success() {
        // CONFIRM constant only available on API 30+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            performSafeHaptic(HapticFeedbackConstants.CONFIRM)
        } else {
            // Fallback to standard click on older devices
            click()
        }
    }
    
    fun error() {
        // REJECT constant only available on API 30+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            performSafeHaptic(HapticFeedbackConstants.REJECT)
        } else {
            // Fallback to long press on older devices
            longPress()
        }
    }
    
    fun selection() {
        performSafeHaptic(HapticFeedbackConstants.KEYBOARD_TAP)
    }
}

@Composable
fun rememberHapticFeedback(): HapticFeedback {
    val view = LocalView.current
    return remember(view) { HapticFeedback(view) }
}
