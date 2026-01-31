package com.resumearchitect.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.resumearchitect.data.models.*

@Database(
    entities = [
        Resume::class,
        PersonalInfo::class,
        WorkExperience::class,
        Education::class,
        Skill::class,
        CustomSection::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ResumeDatabase : RoomDatabase() {
    
    abstract fun resumeDao(): ResumeDao
    abstract fun personalInfoDao(): PersonalInfoDao
    abstract fun workExperienceDao(): WorkExperienceDao
    abstract fun educationDao(): EducationDao
    abstract fun skillDao(): SkillDao
    abstract fun customSectionDao(): CustomSectionDao
    
    companion object {
        const val DATABASE_NAME = "resume_architect.db"
    }
}
