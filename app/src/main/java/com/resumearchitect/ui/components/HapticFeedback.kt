package com.resumearchitect.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView

/**
 * Haptic feedback helper for satisfying tactile responses
 */
class HapticFeedback(private val view: android.view.View) {
    
    fun click() {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }
    
    fun longPress() {
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }
    
    fun success() {
        view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
    }
    
    fun error() {
        view.performHapticFeedback(HapticFeedbackConstants.REJECT)
    }
    
    fun selection() {
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    }
}

@Composable
fun rememberHapticFeedback(): HapticFeedback {
    val view = LocalView.current
    return remember(view) { HapticFeedback(view) }
}
