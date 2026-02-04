package com.resumearchitect.data.models

/**
 * Statistics data for resume insights
 */
data class ResumeStats(
    val totalResumes: Int = 0,
    val completedResumes: Int = 0,
    val totalExports: Int = 0,
    val averageCompletion: Int = 0,
    val mostUsedTemplate: String = "Clarity"
)
