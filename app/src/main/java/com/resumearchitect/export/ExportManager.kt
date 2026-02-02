package com.resumearchitect.export

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.FileProvider
import com.resumearchitect.data.models.*
import com.resumearchitect.data.repository.ResumeRepository
import com.resumearchitect.pdf.PdfGenerator
import com.resumearchitect.pdf.ResumeData
import com.resumearchitect.pdf.TemplateConfig
import com.resumearchitect.templates.TemplateRegistry
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages PDF export and sharing functionality
 */
@Singleton
class ExportManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val pdfGenerator: PdfGenerator,
    private val repository: ResumeRepository
) {
    
    /**
     * Export resume to PDF
     * 
     * @param resumeId Resume to export
     * @param templateId Template to use (optional, uses resume's saved template)
     * @return Result with file path or error
     */
    suspend fun exportResume(
        resumeId: String,
        templateId: String? = null
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            Timber.d("Exporting resume: $resumeId")
            
            // 1. Fetch resume data
            val resumeData = fetchResumeData(resumeId)
                ?: return@withContext Result.failure(Exception("Resume not found"))
            
            // 2. Get template
            val template = getTemplate(resumeId, templateId)
                ?: return@withContext Result.failure(Exception("Template not found"))
            
            // 3. Generate filename
            val fileName = generateFileName(resumeData.personalInfo.fullName)
            
            // 4. Create output file in Downloads
            val outputFile = getOutputFile(fileName)
            
            // 5. Generate PDF
            val result = pdfGenerator.generateResume(resumeData, template, outputFile)
            
            if (result.isSuccess) {
                Timber.d("Resume exported successfully: ${outputFile.absolutePath}")
            } else {
                Timber.e(result.exceptionOrNull(), "Failed to export resume")
            }
            
            result
            
        } catch (e: Exception) {
            Timber.e(e, "Export failed")
            Result.failure(e)
        }
    }
    
    /**
     * Share resume PDF
     * 
     * @param file PDF file to share
     */
    fun shareResume(file: File) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "My Resume")
                putExtra(Intent.EXTRA_TEXT, "Please find my resume attached.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            
            val chooser = Intent.createChooser(shareIntent, "Share Resume")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            
            context.startActivity(chooser)
            Timber.d("Share intent launched")
            
        } catch (e: Exception) {
            Timber.e(e, "Failed to share resume")
        }
    }
    
    /**
     * Email resume as attachment
     * 
     * @param file PDF file
     * @param recipientEmail Optional recipient email
     */
    fun emailResume(file: File, recipientEmail: String? = null) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Resume - ${file.nameWithoutExtension}")
                putExtra(Intent.EXTRA_TEXT, "Please find my resume attached.\n\nBest regards")
                
                recipientEmail?.let {
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(it))
                }
                
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            
            val chooser = Intent.createChooser(emailIntent, "Email Resume")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            
            context.startActivity(chooser)
            
        } catch (e: Exception) {
            Timber.e(e, "Failed to email resume")
        }
    }
    
    /**
     * Fetch all resume data from database
     */
    private suspend fun fetchResumeData(resumeId: String): ResumeData? {
        return try {
            val personalInfo = repository.getPersonalInfoSync(resumeId)
                ?: return null
            
            val workExperiences = repository.getWorkExperiencesSync(resumeId)
            val educations = repository.getEducationsSync(resumeId)
            val skills = repository.getSkillsSync(resumeId)
            val customSections = repository.getCustomSectionsSync(resumeId)
            
            ResumeData(
                personalInfo = personalInfo,
                workExperiences = workExperiences,
                educations = educations,
                skills = skills,
                customSections = customSections
            )
        } catch (e: Exception) {
            Timber.e(e, "Failed to fetch resume data")
            null
        }
    }
    
    /**
     * Get template configuration
     */
    private suspend fun getTemplate(resumeId: String, templateId: String?): TemplateConfig? {
        // Use provided template ID, or get from resume, or use default
        val id = templateId 
            ?: repository.getResumeSync(resumeId)?.templateId 
            ?: "clarity" // Default template
        
        return TemplateRegistry.getTemplateById(id)
    }
    
    /**
     * Generate safe filename
     */
    private fun generateFileName(fullName: String): String {
        val safeName = fullName
            .replace(Regex("[^a-zA-Z0-9\\s]"), "")
            .replace(Regex("\\s+"), "_")
            .take(30)
        
        val timestamp = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            .format(Date())
        
        return "${safeName}_Resume_$timestamp.pdf"
    }
    
    /**
     * Get output file in Downloads directory
     */
    private fun getOutputFile(fileName: String): File {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )
        
        // Ensure directory exists
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs()
        }
        
        return File(downloadsDir, fileName)
    }
    
    /**
     * Get cache file for preview (not saved to Downloads)
     */
    fun getPreviewFile(resumeId: String): File {
        val cacheDir = context.cacheDir
        return File(cacheDir, "preview_$resumeId.pdf")
    }
    
    /**
     * Generate preview PDF (cached, not saved to Downloads)
     */
    suspend fun generatePreview(
        resumeId: String,
        templateId: String? = null
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val resumeData = fetchResumeData(resumeId)
                ?: return@withContext Result.failure(Exception("Resume not found"))
            
            val template = getTemplate(resumeId, templateId)
                ?: return@withContext Result.failure(Exception("Template not found"))
            
            val previewFile = getPreviewFile(resumeId)
            
            pdfGenerator.generateResume(resumeData, template, previewFile)
            
        } catch (e: Exception) {
            Timber.e(e, "Preview generation failed")
            Result.failure(e)
        }
    }
}

// Extension functions for synchronous repository access
private suspend fun ResumeRepository.getResumeSync(resumeId: String): Resume? {
    return getResumeById(resumeId).first()
}

private suspend fun ResumeRepository.getPersonalInfoSync(resumeId: String): PersonalInfo? {
    return getPersonalInfo(resumeId).first()
}

private suspend fun ResumeRepository.getWorkExperiencesSync(resumeId: String): List<WorkExperience> {
    return getWorkExperiences(resumeId).first()
}

private suspend fun ResumeRepository.getEducationsSync(resumeId: String): List<Education> {
    return getEducations(resumeId).first()
}

private suspend fun ResumeRepository.getSkillsSync(resumeId: String): List<Skill> {
    return getSkills(resumeId).first()
}

private suspend fun ResumeRepository.getCustomSectionsSync(resumeId: String): List<CustomSection> {
    return getCustomSections(resumeId).first()
}
