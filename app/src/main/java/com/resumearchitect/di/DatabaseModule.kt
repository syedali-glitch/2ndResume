package com.resumearchitect.di

import android.content.Context
import androidx.room.Room
import com.resumearchitect.data.database.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideResumeDatabase(
        @ApplicationContext context: Context
    ): ResumeDatabase {
        return Room.databaseBuilder(
            context,
            ResumeDatabase::class.java,
            ResumeDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    fun provideResumeDao(database: ResumeDatabase): ResumeDao {
        return database.resumeDao()
    }
    
    @Provides
    fun providePersonalInfoDao(database: ResumeDatabase): PersonalInfoDao {
        return database.personalInfoDao()
    }
    
    @Provides
    fun provideWorkExperienceDao(database: ResumeDatabase): WorkExperienceDao {
        return database.workExperienceDao()
    }
    
    @Provides
    fun provideEducationDao(database: ResumeDatabase): EducationDao {
        return database.educationDao()
    }
    
    @Provides
    fun provideSkillDao(database: ResumeDatabase): SkillDao {
        return database.skillDao()
    }
    
    @Provides
    fun provideCustomSectionDao(database: ResumeDatabase): CustomSectionDao {
        return database.customSectionDao()
    }
}
