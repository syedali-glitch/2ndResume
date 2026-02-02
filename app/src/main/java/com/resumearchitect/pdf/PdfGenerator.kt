package com.resumearchitect.pdf

import android.content.Context
import android.graphics.Color as AndroidColor
import com.resumearchitect.data.models.*
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Core PDF generation engine using Apache PDFBox
 * Generates ATS-compliant PDF resumes from structured data
 */
@Singleton
class PdfGenerator @Inject constructor(
    private val context: Context
) {
    
    companion object {
        // Fonts
        val FONT_REGULAR = PDType1Font.HELVETICA
        val FONT_BOLD = PDType1Font.HELVETICA_BOLD
        
        // Page dimensions (A4)
        val PAGE_WIDTH = PDRectangle.A4.width
        val PAGE_HEIGHT = PDRectangle.A4.height
        
        // Margins
        const val MARGIN_LEFT = 50f
        const val MARGIN_RIGHT = 50f
        const val MARGIN_TOP = 50f
        const val MARGIN_BOTTOM = 50f
        
        // Spacing
        const val LINE_HEIGHT = 14f
        const val SECTION_SPACING = 20f
        const val ITEM_SPACING = 12f
        
        // Font sizes
        const val FONT_SIZE_NAME = 24f
        const val FONT_SIZE_CONTACT = 10f
        const val FONT_SIZE_HEADING = 14f
        const val FONT_SIZE_SUBHEADING = 12f
        const val FONT_SIZE_BODY = 10f
        const val FONT_SIZE_SMALL = 9f
    }
    
    /**
     * Generate a PDF resume from resume data
     * 
     * @param resumeData Complete resume data
     * @param templateConfig Template configuration
     * @param outputFile Output file path
     * @return Success/failure
     */
    suspend fun generateResume(
        resumeData: ResumeData,
        templateConfig: TemplateConfig,
        outputFile: File
    ): Result<File> {
        return try {
            val document = PDDocument()
            
            // Initialize PDFBox for Android
            com.tom_roush.pdfbox.android.PDFBoxResourceLoader.init(context)
            
            // Create first page
            val page = PDPage(PDRectangle.A4)
            document.addPage(page)
            
            // Start content stream
            val contentStream = PDPageContentStream(document, page)
            
            // Render based on template
            when (templateConfig.layout) {
                LayoutType.SINGLE_COLUMN -> renderSingleColumn(
                    contentStream,
                    resumeData,
                    templateConfig
                )
                LayoutType.DUAL_COLUMN -> renderDualColumn(
                    contentStream,
                    resumeData,
                    templateConfig
                )
            }
            
            contentStream.close()
            
            // Add metadata
            document.documentInformation.apply {
                title = resumeData.personalInfo.fullName
                subject = "Professional Resume"
                author = resumeData.personalInfo.fullName
                creator = "Resume Architect"
            }
            
            // Save to file
            document.save(outputFile)
            document.close()
            
            Result.success(outputFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Render single-column layout (ATS-friendly)
     */
    private fun renderSingleColumn(
        contentStream: PDPageContentStream,
        data: ResumeData,
        config: TemplateConfig
    ) {
        var yPosition = PAGE_HEIGHT - MARGIN_TOP
        val font = FONT_REGULAR
        val fontBold = FONT_BOLD
        
        // Personal Info Header
        yPosition = renderHeader(contentStream, data.personalInfo, yPosition, fontBold, font, config)
        
        // Professional Summary
        if (data.personalInfo.summary.isNotBlank()) {
            yPosition = renderSection(
                contentStream,
                "PROFESSIONAL SUMMARY",
                data.personalInfo.summary,
                yPosition,
                fontBold,
                font,
                config
            )
        }
        
        // Work Experience
        if (data.workExperiences.isNotEmpty()) {
            yPosition = renderWorkExperience(
                contentStream,
                data.workExperiences,
                yPosition,
                fontBold,
                font,
                config
            )
        }
        
        // Education
        if (data.educations.isNotEmpty()) {
            yPosition = renderEducation(
                contentStream,
                data.educations,
                yPosition,
                fontBold,
                font,
                config
            )
        }
        
        // Skills
        if (data.skills.isNotEmpty()) {
            yPosition = renderSkills(
                contentStream,
                data.skills,
                yPosition,
                fontBold,
                font,
                config
            )
        }
    }
    
    /**
     * Render dual-column layout
     */
    private fun renderDualColumn(
        contentStream: PDPageContentStream,
        data: ResumeData,
        config: TemplateConfig
    ) {
        // TODO: Implement dual-column layout
        // For now, fallback to single column
        renderSingleColumn(contentStream, data, config)
    }
    
    /**
     * Render header with personal information
     */
    private fun renderHeader(
        contentStream: PDPageContentStream,
        personalInfo: PersonalInfo,
        startY: Float,
        fontBold: PDType1Font,
        font: PDType1Font,
        config: TemplateConfig
    ): Float {
        var yPos = startY
        
        // Name
        contentStream.apply {
            beginText()
            setFont(fontBold, 24f)
            setNonStrokingColor(config.colorScheme.primaryColor)
            newLineAtOffset(MARGIN_LEFT, yPos)
            showText(personalInfo.fullName)
            endText()
        }
        yPos -= 30
        
        // Professional Title
        if (personalInfo.professionalTitle.isNotBlank()) {
            contentStream.apply {
                beginText()
                setFont(font, 14f)
                setNonStrokingColor(config.colorScheme.secondaryColor)
                newLineAtOffset(MARGIN_LEFT, yPos)
                showText(personalInfo.professionalTitle)
                endText()
            }
            yPos -= 25
        }
        
        // Contact Info
        val contactInfo = buildString {
            if (personalInfo.email.isNotBlank()) append("${personalInfo.email} ")
            if (personalInfo.phone.isNotBlank()) append("| ${personalInfo.phone} ")
            if (personalInfo.city.isNotBlank()) append("| ${personalInfo.city}")
        }
        
        if (contactInfo.isNotBlank()) {
            contentStream.apply {
                beginText()
                setFont(font, 10f)
                setNonStrokingColor(config.colorScheme.textColor)
                newLineAtOffset(MARGIN_LEFT, yPos)
                showText(contactInfo.trim())
                endText()
            }
            yPos -= 20
        }
        
        // Separator line
        contentStream.apply {
            setStrokingColor(config.colorScheme.accentColor)
            setLineWidth(2f)
            moveTo(MARGIN_LEFT, yPos)
            lineTo(PAGE_WIDTH - MARGIN_RIGHT, yPos)
            stroke()
        }
        yPos -= 20
        
        return yPos
    }
    
    /**
     * Render a section with title and content
     */
    private fun renderSection(
        contentStream: PDPageContentStream,
        title: String,
        content: String,
        startY: Float,
        fontBold: PDType1Font,
        font: PDType1Font,
        config: TemplateConfig
    ): Float {
        var yPos = startY
        
        // Section title
        contentStream.apply {
            beginText()
            setFont(fontBold, 12f)
            setNonStrokingColor(config.colorScheme.primaryColor)
            newLineAtOffset(MARGIN_LEFT, yPos)
            showText(title)
            endText()
        }
        yPos -= 20
        
        // Section content (word wrap needed for real implementation)
        contentStream.apply {
            beginText()
            setFont(font, 10f)
            setNonStrokingColor(config.colorScheme.textColor)
            newLineAtOffset(MARGIN_LEFT, yPos)
            showText(content.take(80)) // Simplified - needs word wrap
            endText()
        }
        yPos -= 25
        
        return yPos
    }
    
    /**
     * Render work experience section
     */
    private fun renderWorkExperience(
        contentStream: PDPageContentStream,
        experiences: List<WorkExperience>,
        startY: Float,
        fontBold: PDType1Font,
        font: PDType1Font,
        config: TemplateConfig
    ): Float {
        var yPos = startY
        
        // Section title
        contentStream.apply {
            beginText()
            setFont(fontBold, 12f)
            setNonStrokingColor(config.colorScheme.primaryColor)
            newLineAtOffset(MARGIN_LEFT, yPos)
            showText("WORK EXPERIENCE")
            endText()
        }
        yPos -= 20
        
        // Render each experience (simplified)
        experiences.take(3).forEach { exp ->
            // Position & Company
            contentStream.apply {
                beginText()
                setFont(fontBold, 11f)
                setNonStrokingColor(config.colorScheme.textColor)
                newLineAtOffset(MARGIN_LEFT, yPos)
                showText(exp.position)
                endText()
            }
            yPos -= 15
            
            // Company and dates
            contentStream.apply {
                beginText()
                setFont(font, 10f)
                setNonStrokingColor(config.colorScheme.secondaryColor)
                newLineAtOffset(MARGIN_LEFT, yPos)
                showText("${exp.company} | ${formatDate(exp.startDate)} - ${if (exp.isCurrentPosition) "Present" else formatDate(exp.endDate)}")
                endText()
            }
            yPos -= 15
            
            // Achievements (simplified - just first 2)
            exp.achievements.take(2).forEach { achievement ->
                contentStream.apply {
                    beginText()
                    setFont(font, 10f)
                    setNonStrokingColor(config.colorScheme.textColor)
                    newLineAtOffset(MARGIN_LEFT + 15, yPos)
                    showText("• ${achievement.take(60)}") // Truncated for simplicity
                    endText()
                }
                yPos -= 12
            }
            
            yPos -= 10
        }
        
        return yPos
    }
    
    /**
     * Render education section
     */
    private fun renderEducation(
        contentStream: PDPageContentStream,
        educations: List<Education>,
        startY: Float,
        fontBold: PDType1Font,
        font: PDType1Font,
        config: TemplateConfig
    ): Float {
        var yPos = startY
        
        // Section title
        contentStream.apply {
            beginText()
            setFont(fontBold, 12f)
            setNonStrokingColor(config.colorScheme.primaryColor)
            newLineAtOffset(MARGIN_LEFT, yPos)
            showText("EDUCATION")
            endText()
        }
        yPos -= 20
        
        // Render each education
        educations.forEach { edu ->
            contentStream.apply {
                beginText()
                setFont(fontBold, 11f)
                setNonStrokingColor(config.colorScheme.textColor)
                newLineAtOffset(MARGIN_LEFT, yPos)
                showText("${edu.degree} in ${edu.fieldOfStudy}")
                endText()
            }
            yPos -= 15
            
            contentStream.apply {
                beginText()
                setFont(font, 10f)
                setNonStrokingColor(config.colorScheme.secondaryColor)
                newLineAtOffset(MARGIN_LEFT, yPos)
                showText("${edu.institution} | ${formatDate(edu.endDate)}")
                endText()
            }
            yPos -= 20
        }
        
        return yPos
    }
    
    /**
     * Render skills section
     */
    private fun renderSkills(
        contentStream: PDPageContentStream,
        skills: List<Skill>,
        startY: Float,
        fontBold: PDType1Font,
        font: PDType1Font,
        config: TemplateConfig
    ): Float {
        var yPos = startY
        
        // Section title
        contentStream.apply {
            beginText()
            setFont(fontBold, 12f)
            setNonStrokingColor(config.colorScheme.primaryColor)
            newLineAtOffset(MARGIN_LEFT, yPos)
            showText("SKILLS")
            endText()
        }
        yPos -= 20
        
        // Group by category
        val groupedSkills = skills.groupBy { it.category }
        
        groupedSkills.forEach { (category, categorySkills) ->
            val skillsText = categorySkills.joinToString(", ") { it.name }
            
            contentStream.apply {
                beginText()
                setFont(font, 10f)
                setNonStrokingColor(config.colorScheme.textColor)
                newLineAtOffset(MARGIN_LEFT, yPos)
                showText("${category.name}: ${skillsText.take(80)}")
                endText()
            }
            yPos -= 15
        }
        
        return yPos
    }
    
    private fun formatDate(timestamp: Long?): String {
        if (timestamp == null) return ""
        val sdf = java.text.SimpleDateFormat("MM/yyyy", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }
}

/**
 * Complete resume data for PDF generation
 */
data class ResumeData(
    val personalInfo: PersonalInfo,
    val workExperiences: List<WorkExperience>,
    val educations: List<Education>,
    val skills: List<Skill>,
    val customSections: List<CustomSection>
)

/**
 * Template configuration
 */
data class TemplateConfig(
    val id: String,
    val name: String,
    val description: String = "",
    val layout: LayoutType,
    val colorScheme: ColorScheme,
    val isPremium: Boolean = false,
    val thumbnailUrl: String? = null
)

enum class LayoutType {
    SINGLE_COLUMN,
    DUAL_COLUMN
}

/**
 * Color scheme for PDF
 */
data class ColorScheme(
    val primaryColor: Int = AndroidColor.rgb(99, 102, 241), // Indigo
    val secondaryColor: Int = AndroidColor.rgb(107, 114, 128), // Gray
    val textColor: Int = AndroidColor.BLACK,
    val accentColor: Int = AndroidColor.rgb(99, 102, 241)
)
