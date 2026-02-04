package com.resumearchitect.data.models

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Immutable
@Entity(
    tableName = "skills",
    foreignKeys = [
        ForeignKey(
            entity = Resume::class,
            parentColumns = ["id"],
            childColumns = ["resumeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("resumeId")]
)
data class Skill(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val resumeId: String,
    val name: String,
    val category: SkillCategory = SkillCategory.TECHNICAL,
    val proficiency: ProficiencyLevel? = null,
    val displayOrder: Int = 0
)

enum class SkillCategory {
    TECHNICAL,
    SOFT_SKILLS,
    LANGUAGES,
    TOOLS,
    CERTIFICATIONS,
    CUSTOM
}

enum class ProficiencyLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED,
    EXPERT
}
