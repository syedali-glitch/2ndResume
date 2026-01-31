package com.resumearchitect.data.database

import androidx.room.*
import com.resumearchitect.data.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ResumeDao {
    
    @Query("SELECT * FROM resumes WHERE isActive = 1 ORDER BY updatedAt DESC")
    fun getAllResumesFlow(): Flow<List<Resume>>
    
    @Query("SELECT * FROM resumes WHERE id = :resumeId")
    suspend fun getResumeById(resumeId: String): Resume?
    
    @Query("SELECT * FROM resumes WHERE id = :resumeId")
    fun getResumeByIdFlow(resumeId: String): Flow<Resume?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResume(resume: Resume)
    
    @Update
    suspend fun updateResume(resume: Resume)
    
    @Delete
    suspend fun deleteResume(resume: Resume)
    
    @Query("UPDATE resumes SET isActive = 0 WHERE id = :resumeId")
    suspend fun softDeleteResume(resumeId: String)
    
    @Query("SELECT * FROM resumes WHERE title LIKE '%' || :query || '%'")
    fun searchResumes(query: String): Flow<List<Resume>>
}

@Dao
interface PersonalInfoDao {
    
    @Query("SELECT * FROM personal_info WHERE resumeId = :resumeId LIMIT 1")
    fun getPersonalInfoFlow(resumeId: String): Flow<PersonalInfo?>
    
    @Query("SELECT * FROM personal_info WHERE resumeId = :resumeId LIMIT 1")
    suspend fun getPersonalInfo(resumeId: String): PersonalInfo?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonalInfo(personalInfo: PersonalInfo)
    
    @Update
    suspend fun updatePersonalInfo(personalInfo: PersonalInfo)
    
    @Delete
    suspend fun deletePersonalInfo(personalInfo: PersonalInfo)
}

@Dao
interface WorkExperienceDao {
    
    @Query("SELECT * FROM work_experience WHERE resumeId = :resumeId ORDER BY displayOrder ASC")
    fun getWorkExperiencesFlow(resumeId: String): Flow<List<WorkExperience>>
    
    @Query("SELECT * FROM work_experience WHERE resumeId = :resumeId ORDER BY displayOrder ASC")
    suspend fun getWorkExperiences(resumeId: String): List<WorkExperience>
    
    @Query("SELECT * FROM work_experience WHERE id = :id")
    suspend fun getWorkExperienceById(id: String): WorkExperience?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkExperience(workExperience: WorkExperience)
    
    @Update
    suspend fun updateWorkExperience(workExperience: WorkExperience)
    
    @Delete
    suspend fun deleteWorkExperience(workExperience: WorkExperience)
    
    @Transaction
    suspend fun updateDisplayOrders(experiences: List<WorkExperience>) {
        experiences.forEachIndexed { index, experience ->
            updateWorkExperience(experience.copy(displayOrder = index))
        }
    }
}

@Dao
interface EducationDao {
    
    @Query("SELECT * FROM education WHERE resumeId = :resumeId ORDER BY displayOrder ASC")
    fun getEducationsFlow(resumeId: String): Flow<List<Education>>
    
    @Query("SELECT * FROM education WHERE resumeId = :resumeId ORDER BY displayOrder ASC")
    suspend fun getEducations(resumeId: String): List<Education>
    
    @Query("SELECT * FROM education WHERE id = :id")
    suspend fun getEducationById(id: String): Education?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEducation(education: Education)
    
    @Update
    suspend fun updateEducation(education: Education)
    
    @Delete
    suspend fun deleteEducation(education: Education)
    
    @Transaction
    suspend fun updateDisplayOrders(educations: List<Education>) {
        educations.forEachIndexed { index, education ->
            updateEducation(education.copy(displayOrder = index))
        }
    }
}

@Dao
interface SkillDao {
    
    @Query("SELECT * FROM skills WHERE resumeId = :resumeId ORDER BY category ASC, displayOrder ASC")
    fun getSkillsFlow(resumeId: String): Flow<List<Skill>>
    
    @Query("SELECT * FROM skills WHERE resumeId = :resumeId ORDER BY category ASC, displayOrder ASC")
    suspend fun getSkills(resumeId: String): List<Skill>
    
    @Query("SELECT * FROM skills WHERE resumeId = :resumeId AND category = :category ORDER BY displayOrder ASC")
    fun getSkillsByCategory(resumeId: String, category: SkillCategory): Flow<List<Skill>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkill(skill: Skill)
    
    @Update
    suspend fun updateSkill(skill: Skill)
    
    @Delete
    suspend fun deleteSkill(skill: Skill)
}

@Dao
interface CustomSectionDao {
    
    @Query("SELECT * FROM custom_sections WHERE resumeId = :resumeId ORDER BY displayOrder ASC")
    fun getCustomSectionsFlow(resumeId: String): Flow<List<CustomSection>>
    
    @Query("SELECT * FROM custom_sections WHERE resumeId = :resumeId ORDER BY displayOrder ASC")
    suspend fun getCustomSections(resumeId: String): List<CustomSection>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomSection(customSection: CustomSection)
    
    @Update
    suspend fun updateCustomSection(customSection: CustomSection)
    
    @Delete
    suspend fun deleteCustomSection(customSection: CustomSection)
    
    @Transaction
    suspend fun updateDisplayOrders(sections: List<CustomSection>) {
        sections.forEachIndexed { index, section ->
            updateCustomSection(section.copy(displayOrder = index))
        }
    }
}
