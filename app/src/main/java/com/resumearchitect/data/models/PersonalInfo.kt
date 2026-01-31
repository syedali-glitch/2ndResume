package com.resumearchitect.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "personal_info",
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
data class PersonalInfo(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val resumeId: String,
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val zipCode: String = "",
    val country: String = "",
    val linkedIn: String = "",
    val portfolio: String = "",
    val github: String = "",
    val professionalTitle: String = "",
    val profileImageUri: String? = null,
    val summary: String = ""
)
