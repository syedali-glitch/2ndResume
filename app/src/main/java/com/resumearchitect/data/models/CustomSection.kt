package com.resumearchitect.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.resumearchitect.data.database.Converters
import java.util.UUID

@Entity(
    tableName = "custom_sections",
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
data class CustomSection(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val resumeId: String,
    val title: String,
    val contentType: ContentType = ContentType.BULLET_LIST,
    val content: List<String> = emptyList(),
    val isVisible: Boolean = true,
    val displayOrder: Int = 0
)

enum class ContentType {
    PARAGRAPH,
    BULLET_LIST,
    TABLE,
    CUSTOM
}
