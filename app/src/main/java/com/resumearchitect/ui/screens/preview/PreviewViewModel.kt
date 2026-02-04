package com.resumearchitect.ui.screens.preview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resumearchitect.data.models.Resume
import com.resumearchitect.data.repository.ResumeRepository
import com.resumearchitect.export.ExportManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val repository: ResumeRepository,
    private val exportManager: ExportManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val resumeId: String = checkNotNull(savedStateHandle["resumeId"])
    
    val resume: StateFlow<Resume?> = repository.getResumeById(resumeId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    
    private val _previewState = MutableStateFlow<PreviewState>(PreviewState.Loading)
    val previewState: StateFlow<PreviewState> = _previewState.asStateFlow()
    
    private val _exportState = MutableStateFlow<ExportState>(ExportState.Idle)
    val exportState: StateFlow<ExportState> = _exportState.asStateFlow()
    
    init {
        // Observe resume and generate preview when it becomes available
        viewModelScope.launch {
            resume.filterNotNull().first()
            generatePreview()
        }
    }
    
    /**
     * Generate PDF preview
     */
    fun generatePreview(templateId: String? = null) {
        viewModelScope.launch {
            _previewState.value = PreviewState.Loading
            
            try {
                // Safety check: ensure resume exists
                val currentResume = resume.value
                if (currentResume == null) {
                    _previewState.value = PreviewState.Error("Resume not found. Please try again.")
                    return@launch
                }
                
                val result = exportManager.generatePreview(resumeId, templateId)
                
                _previewState.value = if (result.isSuccess) {
                    PreviewState.Success(result.getOrNull()!!)
                } else {
                    PreviewState.Error(result.exceptionOrNull()?.message ?: "Failed to generate preview")
                }
            } catch (e: Exception) {
                _previewState.value = PreviewState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
    
    /**
     * Update resume theme settings
     */
    fun updateTheme(templateId: String, colorSchemeId: String) {
        viewModelScope.launch {
            val currentResume = resume.value ?: return@launch
            
            val updatedResume = currentResume.copy(
                templateId = templateId,
                colorSchemeId = colorSchemeId
            )
            
            repository.updateResume(updatedResume)
            // Regenerate preview with new settings
            generatePreview(templateId)
        }
    }
    
    /**
     * Export resume to Downloads
     */
    fun exportResume() {
        viewModelScope.launch {
            _exportState.value = ExportState.Exporting
            
            val result = exportManager.exportResume(resumeId)
            
            _exportState.value = if (result.isSuccess) {
                ExportState.Success(result.getOrNull()!!)
            } else {
                ExportState.Error(result.exceptionOrNull()?.message ?: "Export failed")
            }
        }
    }
    
    /**
     * Share resume
     */
    fun shareResume() {
        viewModelScope.launch {
            val state = previewState.value
            if (state is PreviewState.Success) {
                exportManager.shareResume(state.file)
            }
        }
    }
    
    /**
     * Email resume
     */
    fun emailResume(recipientEmail: String? = null) {
        viewModelScope.launch {
            val state = previewState.value
            if (state is PreviewState.Success) {
                exportManager.emailResume(state.file, recipientEmail)
            }
        }
    }
    
    /**
     * Reset export state
     */
    fun resetExportState() {
        _exportState.value = ExportState.Idle
    }
}

sealed class PreviewState {
    object Loading : PreviewState()
    data class Success(val file: File) : PreviewState()
    data class Error(val message: String) : PreviewState()
}

sealed class ExportState {
    object Idle : ExportState()
    object Exporting : ExportState()
    data class Success(val file: File) : ExportState()
    data class Error(val message: String) : ExportState()
}
