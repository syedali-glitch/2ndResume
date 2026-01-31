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
)
