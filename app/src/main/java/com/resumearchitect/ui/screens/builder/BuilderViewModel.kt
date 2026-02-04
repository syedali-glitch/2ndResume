package com.resumearchitect.ui.screens.builder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resumearchitect.data.models.*
import com.resumearchitect.data.repository.ResumeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuilderViewModel @Inject constructor(
    private val repository: ResumeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val resumeId: String = checkNotNull(savedStateHandle["resumeId"])
    
    // Resume data
    val resume: StateFlow<Resume?> = repository.getResumeById(resumeId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    
    val personalInfo: StateFlow<PersonalInfo?> = repository.getPersonalInfo(resumeId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    
    val workExperiences: StateFlow<List<WorkExperience>> = 
        repository.getWorkExperiences(resumeId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val educations: StateFlow<List<Education>> = 
        repository.getEducations(resumeId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val skills: StateFlow<List<Skill>> = 
        repository.getSkills(resumeId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val customSections: StateFlow<List<CustomSection>> = 
        repository.getCustomSections(resumeId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    // Completion Percentage
    val completionPercentage: StateFlow<Int> = combine(
        resume,
        personalInfo,
        workExperiences,
        educations,
        skills
    ) { r, pInfo, exp, edu, sk ->
        r?.calculateCompletion(
            hasContact = pInfo?.email?.isNotBlank() == true,
            experienceCount = exp.size,
            educationCount = edu.size,
            skillsCount = sk.size,
            hasSummary = pInfo?.summary?.isNotBlank() == true
        ) ?: 0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    // UI State
    private val _currentSection = MutableStateFlow(BuilderSection.CONTACT)
    val currentSection: StateFlow<BuilderSection> = _currentSection.asStateFlow()
    
    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()
    
    // Navigate to section
    fun navigateToSection(section: BuilderSection) {
        _currentSection.value = section
    }
    
    // Personal Info operations
    fun updatePersonalInfo(personalInfo: PersonalInfo) {
        viewModelScope.launch {
            _isSaving.value = true
            repository.updatePersonalInfo(personalInfo)
            _isSaving.value = false
        }
    }
    
    // Work Experience operations
    fun addWorkExperience(experience: WorkExperience) {
        viewModelScope.launch {
            repository.addWorkExperience(experience.copy(resumeId = resumeId))
        }
    }
    
    fun updateWorkExperience(experience: WorkExperience) {
        viewModelScope.launch {
            repository.updateWorkExperience(experience)
        }
    }
    
    fun deleteWorkExperience(experience: WorkExperience) {
        viewModelScope.launch {
            repository.deleteWorkExperience(experience)
        }
    }
    
    fun reorderWorkExperiences(experiences: List<WorkExperience>) {
        viewModelScope.launch {
            repository.reorderWorkExperiences(experiences)
        }
    }
    
    // Education operations
    fun addEducation(education: Education) {
        viewModelScope.launch {
            repository.addEducation(education.copy(resumeId = resumeId))
        }
    }
    
    fun updateEducation(education: Education) {
        viewModelScope.launch {
            repository.updateEducation(education)
        }
    }
    
    fun deleteEducation(education: Education) {
        viewModelScope.launch {
            repository.deleteEducation(education)
        }
    }
    
    fun reorderEducations(educations: List<Education>) {
        viewModelScope.launch {
            repository.reorderEducations(educations)
        }
    }
    
    // Skill operations
    fun addSkill(skill: Skill) {
        viewModelScope.launch {
            repository.addSkill(skill.copy(resumeId = resumeId))
        }
    }
    
    fun deleteSkill(skill: Skill) {
        viewModelScope.launch {
            repository.deleteSkill(skill)
        }
    }
    
    // Custom Section operations
    fun addCustomSection(section: CustomSection) {
        viewModelScope.launch {
            repository.addCustomSection(section.copy(resumeId = resumeId))
        }
    }
    
    fun updateCustomSection(section: CustomSection) {
        viewModelScope.launch {
            repository.updateCustomSection(section)
        }
    }
    
    fun deleteCustomSection(section: CustomSection) {
        viewModelScope.launch {
            repository.deleteCustomSection(section)
        }
    }
    
    fun updateResumeTitle(title: String) {
        viewModelScope.launch {
            resume.value?.let { currentResume ->
                repository.updateResume(currentResume.copy(title = title))
            }
        }
    }
}

enum class BuilderSection {
    CONTACT,
    SUMMARY,
    EXPERIENCE,
    EDUCATION,
    SKILLS,
    CUSTOM
}
