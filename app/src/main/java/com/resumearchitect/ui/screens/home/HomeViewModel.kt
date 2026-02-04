package com.resumearchitect.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resumearchitect.data.models.Resume
import com.resumearchitect.data.models.ResumeStats
import com.resumearchitect.data.repository.ResumeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ResumeRepository
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _resumes = repository.getAllResumes()
    
    val resumes: StateFlow<List<Resume>> = combine(
        _resumes,
        _searchQuery
    ) { resumes, query ->
        if (query.isBlank()) {
            resumes
        } else {
            resumes.filter { resume ->
                resume.title.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    // Derived state for quick empty check (avoids unnecessary recompositions)
    val hasResumes: StateFlow<Boolean> = resumes.map { it.isNotEmpty() }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    
    // Statistics calculation
    val statistics: StateFlow<ResumeStats> = _resumes.map { resumeList ->
        ResumeStats(
            totalResumes = resumeList.size,
            completedResumes = resumeList.count { resume ->
                // Consider >80% as completed
                // For now, we'll use a simple metric
                resumeList.size > 0
            },
            totalExports = 0, // TODO: Track exports in database
            averageCompletion = if (resumeList.isEmpty()) 0 else 65, // Placeholder
            mostUsedTemplate = "Clarity"
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ResumeStats()
    )
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun createResume(templateId: String = "clarity", colorSchemeId: String = "professional-blue") {
        viewModelScope.launch {
            val count = resumes.value.size + 1
            repository.createResume(
                title = "My Resume $count",
                templateId = templateId,
                colorSchemeId = colorSchemeId
            )
        }
    }
    
    suspend fun createResumeForTheme(templateId: String, colorSchemeId: String): String {
        val count = resumes.value.size + 1
        val resume = repository.createResume(
            title = "My Resume $count",
            templateId = templateId,
            colorSchemeId = colorSchemeId
        )
        return resume.id
    }
    
    fun duplicateResume(resume: Resume) {
        viewModelScope.launch {
            repository.duplicateResume(resume.id)
        }
    }
    
    fun deleteResume(resume: Resume) {
        viewModelScope.launch {
            repository.deleteResume(resume)
        }
    }
}
