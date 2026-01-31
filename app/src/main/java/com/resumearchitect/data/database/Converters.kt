package com.resumearchitect.data.database

import androidx.room.TypeConverter
import com.resumearchitect.data.models.ContentType
import com.resumearchitect.data.models.EmploymentType
import com.resumearchitect.data.models.ProficiencyLevel
import com.resumearchitect.data.models.SkillCategory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    
    private val json = Json { ignoreUnknownKeys = true }
    
    // List<String> converters
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return json.encodeToString(value)
    }
    
    @TypeConverter
    fun toStringList(value: String): List<String> {
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // EmploymentType converters
    @TypeConverter
    fun fromEmploymentType(value: EmploymentType): String {
        return value.name
    }
    
    @TypeConverter
    fun toEmploymentType(value: String): EmploymentType {
        return try {
            EmploymentType.valueOf(value)
        } catch (e: Exception) {
            EmploymentType.FULL_TIME
        }
    }
    
    // SkillCategory converters
    @TypeConverter
    fun fromSkillCategory(value: SkillCategory): String {
        return value.name
    }
    
    @TypeConverter
    fun toSkillCategory(value: String): SkillCategory {
        return try {
            SkillCategory.valueOf(value)
        } catch (e: Exception) {
            SkillCategory.TECHNICAL
        }
    }
    
    // ProficiencyLevel converters
    @TypeConverter
    fun fromProficiencyLevel(value: ProficiencyLevel?): String? {
        return value?.name
    }
    
    @TypeConverter
    fun toProficiencyLevel(value: String?): ProficiencyLevel? {
        return value?.let {
            try {
                ProficiencyLevel.valueOf(it)
            } catch (e: Exception) {
                null
            }
        }
    }
    
    // ContentType converters
    @TypeConverter
    fun fromContentType(value: ContentType): String {
        return value.name
    }
    
    @TypeConverter
    fun toContentType(value: String): ContentType {
        return try {
            ContentType.valueOf(value)
        } catch (e: Exception) {
            ContentType.BULLET_LIST
        }
    }
}
