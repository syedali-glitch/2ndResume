package com.resumearchitect.data.repository

import com.resumearchitect.data.database.*
import com.resumearchitect.data.models.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResumeRepository @Inject constructor(
    private val resumeDao: ResumeDao,
    private val personalInfoDao: PersonalInfoDao,
    private val workExperienceDao: WorkExperienceDao,
    private val educationDao: EducationDao,
    private val skillDao: SkillDao,
    private val customSectionDao: CustomSectionDao
) {
    
    // Resume operations
    fun getAllResumes(): Flow<List<Resume>> = resumeDao.getAllResumesFlow()
    
    fun getResumeById(resumeId: String): Flow<Resume?> = resumeDao.getResumeByIdFlow(resumeId)
    
    suspend fun createResume(title: String, templateId: String, colorSchemeId: String): Resume {
        val resume = Resume(
            title = title,
            templateId = templateId,
            colorSchemeId = colorSchemeId
        )
        resumeDao.insertResume(resume)
        
        // Create default personal info
        personalInfoDao.insertPersonalInfo(
            PersonalInfo(resumeId = resume.id)
        )
        
        return resume
    }
    
    suspend fun updateResume(resume: Resume) {
        resumeDao.updateResume(resume.copy(updatedAt = System.currentTimeMillis()))
    }
    
    suspend fun deleteResume(resume: Resume) {
        resumeDao.softDeleteResume(resume.id)
    }
    
    suspend fun duplicateResume(resumeId: String): Resume? {
        val originalResume = resumeDao.getResumeById(resumeId) ?: return null
        
        val newResume = originalResume.copy(
            id = java.util.UUID.randomUUID().toString(),
            title = "${originalResume.title} (Copy)",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        resumeDao.insertResume(newResume)
        
        // Duplicate personal info
        personalInfoDao.getPersonalInfo(resumeId)?.let { info ->
            personalInfoDao.insertPersonalInfo(
                info.copy(
                    id = java.util.UUID.randomUUID().toString(),
                    resumeId = newResume.id
                )
            )
        }
        
        // Duplicate work experiences
        workExperienceDao.getWorkExperiences(resumeId).forEach { exp ->
            workExperienceDao.insertWorkExperience(
                exp.copy(
                    id = java.util.UUID.randomUUID().toString(),
                    resumeId = newResume.id
                )
            )
        }
        
        // Duplicate education
        educationDao.getEducations(resumeId).forEach { edu ->
            educationDao.insertEducation(
                edu.copy(
                    id = java.util.UUID.randomUUID().toString(),
                    resumeId = newResume.id
                )
            )
        }
        
        // Duplicate skills
        skillDao.getSkills(resumeId).forEach { skill ->
            skillDao.insertSkill(
                skill.copy(
                    id = java.util.UUID.randomUUID().toString(),
                    resumeId = newResume.id
                )
            )
        }
        
        // Duplicate custom sections
        customSectionDao.getCustomSections(resumeId).forEach { section ->
            customSectionDao.insertCustomSection(
                section.copy(
                    id = java.util.UUID.randomUUID().toString(),
                    resumeId = newResume.id
                )
            )
        }
        
        return newResume
    }
    
    fun searchResumes(query: String): Flow<List<Resume>> = resumeDao.searchResumes(query)
    
    // Personal Info operations
    fun getPersonalInfo(resumeId: String): Flow<PersonalInfo?> = 
        personalInfoDao.getPersonalInfoFlow(resumeId)
    
    suspend fun updatePersonalInfo(personalInfo: PersonalInfo) {
        personalInfoDao.insertPersonalInfo(personalInfo)
        updateResumeTimestamp(personalInfo.resumeId)
    }
    
    // Work Experience operations
    fun getWorkExperiences(resumeId: String): Flow<List<WorkExperience>> = 
        workExperienceDao.getWorkExperiencesFlow(resumeId)
    
    suspend fun addWorkExperience(workExperience: WorkExperience) {
        workExperienceDao.insertWorkExperience(workExperience)
        updateResumeTimestamp(workExperience.resumeId)
    }
    
    suspend fun updateWorkExperience(workExperience: WorkExperience) {
        workExperienceDao.updateWorkExperience(workExperience)
        updateResumeTimestamp(workExperience.resumeId)
    }
    
    suspend fun deleteWorkExperience(workExperience: WorkExperience) {
        workExperienceDao.deleteWorkExperience(workExperience)
        updateResumeTimestamp(workExperience.resumeId)
    }
    
    suspend fun reorderWorkExperiences(experiences: List<WorkExperience>) {
        workExperienceDao.updateDisplayOrders(experiences)
    }
    
    // Education operations
    fun getEducations(resumeId: String): Flow<List<Education>> = 
        educationDao.getEducationsFlow(resumeId)
    
    suspend fun addEducation(education: Education) {
        educationDao.insertEducation(education)
        updateResumeTimestamp(education.resumeId)
    }
    
    suspend fun updateEducation(education: Education) {
        educationDao.updateEducation(education)
        updateResumeTimestamp(education.resumeId)
    }
    
    suspend fun deleteEducation(education: Education) {
        educationDao.deleteEducation(education)
        updateResumeTimestamp(education.resumeId)
    }
    
    suspend fun reorderEducations(educations: List<Education>) {
        educationDao.updateDisplayOrders(educations)
    }
    
    // Skill operations
    fun getSkills(resumeId: String): Flow<List<Skill>> = 
        skillDao.getSkillsFlow(resumeId)
    
    fun getSkillsByCategory(resumeId: String, category: SkillCategory): Flow<List<Skill>> = 
        skillDao.getSkillsByCategory(resumeId, category)
    
    suspend fun addSkill(skill: Skill) {
        skillDao.insertSkill(skill)
        updateResumeTimestamp(skill.resumeId)
    }
    
    suspend fun updateSkill(skill: Skill) {
        skillDao.updateSkill(skill)
        updateResumeTimestamp(skill.resumeId)
    }
    
    suspend fun deleteSkill(skill: Skill) {
        skillDao.deleteSkill(skill)
        updateResumeTimestamp(skill.resumeId)
    }
    
    // Custom Section operations
    fun getCustomSections(resumeId: String): Flow<List<CustomSection>> = 
        customSectionDao.getCustomSectionsFlow(resumeId)
    
    suspend fun addCustomSection(customSection: CustomSection) {
        customSectionDao.insertCustomSection(customSection)
        updateResumeTimestamp(customSection.resumeId)
    }
    
    suspend fun updateCustomSection(customSection: CustomSection) {
        customSectionDao.updateCustomSection(customSection)
        updateResumeTimestamp(customSection.resumeId)
    }
    
    suspend fun deleteCustomSection(customSection: CustomSection) {
        customSectionDao.deleteCustomSection(customSection)
        updateResumeTimestamp(customSection.resumeId)
    }
    
    suspend fun reorderCustomSections(sections: List<CustomSection>) {
        customSectionDao.updateDisplayOrders(sections)
    }
    
    // Helper function to update resume timestamp
    private suspend fun updateResumeTimestamp(resumeId: String) {
        resumeDao.getResumeById(resumeId)?.let { resume ->
            resumeDao.updateResume(resume.copy(updatedAt = System.currentTimeMillis()))
        }
    }
    
    // Synchronous data fetching for export (used by ExportManager)
    suspend fun getResumeSync(resumeId: String): Resume? {
        return resumeDao.getResumeById(resumeId)
    }
    
    suspend fun getPersonalInfoSync(resumeId: String): PersonalInfo? {
        return personalInfoDao.getPersonalInfo(resumeId)
    }
    
    suspend fun getWorkExperiencesSync(resumeId: String): List<WorkExperience> {
        return workExperienceDao.getWorkExperiences(resumeId)
    }
    
    suspend fun getEducationsSync(resumeId: String): List<Education> {
        return educationDao.getEducations(resumeId)
    }
    
    suspend fun getSkillsSync(resumeId: String): List<Skill> {
        return skillDao.getSkills(resumeId)
    }
    
    suspend fun getCustomSectionsSync(resumeId: String): List<CustomSection> {
        return customSectionDao.getCustomSections(resumeId)
    }
}
