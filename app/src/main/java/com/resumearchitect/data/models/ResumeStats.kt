package com.resumearchitect.data.models

import androidx.compose.runtime.Immutable

/**
 * Statistics data for resume insights
 */
@Immutable
data class ResumeStats(
    val totalResumes: Int = 0,
    val completedResumes: Int = 0,
    val totalExports: Int = 0,
    val averageCompletion: Int = 0,
    val mostUsedTemplate: String = "Clarity"
)
