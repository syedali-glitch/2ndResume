package com.resumearchitect.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "resumes")
data class Resume(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val templateId: String,
    val colorSchemeId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val lastEditedSection: String? = null
) {
    /**
     * Calculate completion percentage based on resume content
     * Contact: 20%, Experience: 30%, Education: 20%, Skills: 15%, Summary: 15%
     */
    fun calculateCompletion(
        hasContact: Boolean,
        experienceCount: Int,
        educationCount: Int,
        skillsCount: Int,
        hasSummary: Boolean
    ): Int {
        var completion = 0
        
        // Contact info (20%)
        if (hasContact) completion += 20
        
        // Experience (30%)
        when {
            experienceCount >= 3 -> completion += 30
            experienceCount >= 2 -> completion += 20
            experienceCount >= 1 -> completion += 10
        }
        
        // Education (20%)
        when {
            educationCount >= 2 -> completion += 20
            educationCount >= 1 -> completion += 10
        }
        
        // Skills (15%)
        when {
            skillsCount >= 5 -> completion += 15
            skillsCount >= 3 -> completion += 10
            skillsCount >= 1 -> completion += 5
        }
        
        // Summary (15%)
        if (hasSummary) completion += 15
        
        return completion.coerceIn(0, 100)
    }
}
