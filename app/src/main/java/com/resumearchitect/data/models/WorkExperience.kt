package com.resumearchitect.data.models

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.resumearchitect.data.database.Converters
import java.util.UUID

@Immutable
@Entity(
    tableName = "work_experience",
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
@TypeConverters(Converters::class)
data class WorkExperience(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val resumeId: String,
    val company: String = "",
    val position: String = "",
    val location: String = "",
    val employmentType: EmploymentType = EmploymentType.FULL_TIME,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val isCurrentPosition: Boolean = false,
    val achievements: List<String> = emptyList(),
    val displayOrder: Int = 0
)

enum class EmploymentType {
    FULL_TIME,
    PART_TIME,
    CONTRACT,
    FREELANCE,
    INTERNSHIP,
    SELF_EMPLOYED
}
