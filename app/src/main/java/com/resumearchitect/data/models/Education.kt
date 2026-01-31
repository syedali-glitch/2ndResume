package com.resumearchitect.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.resumearchitect.data.database.Converters
import java.util.UUID

@Entity(
    tableName = "education",
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
data class Education(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val resumeId: String,
    val institution: String = "",
    val degree: String = "",
    val fieldOfStudy: String = "",
    val location: String = "",
    val startDate: Long? = null,
    val endDate: Long? = null,
    val isCurrentlyEnrolled: Boolean = false,
    val gpa: String? = null,
    val honors: List<String> = emptyList(),
    val relevantCoursework: List<String> = emptyList(),
    val displayOrder: Int = 0
)
