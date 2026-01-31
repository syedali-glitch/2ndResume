package com.resumearchitect.templates

import com.resumearchitect.pdf.ColorScheme
import com.resumearchitect.pdf.LayoutType
import com.resumearchitect.pdf.TemplateConfig

/**
 * Central registry for all resume templates
 * MVP Phase 1: 10 professional templates
 */
object TemplateRegistry {
    
    fun getAllTemplates(): List<TemplateConfig> {
        return listOf(
            // Modern Professional Templates (3)
            clarityTemplate(),
            minimalProTemplate(),
            metroTemplate(),
            
            // Creative Professional Templates (2)
            artisanTemplate(),
            canvasTemplate(),
            
            // Executive/Senior Templates (2)
            executiveSuiteTemplate(),
            boardroomTemplate(),
            
            // Technical/Developer Templates (3)
            codeMasterTemplate(),
            devProTemplate(),
            terminalTemplate()
        )
    }
    
    fun getTemplateById(id: String): TemplateConfig? {
        return getAllTemplates().firstOrNull { it.id == id }
    }
    
    fun getTemplatesByCategory(category: TemplateCategory): List<TemplateConfig> {
        return when (category) {
            TemplateCategory.MODERN_PROFESSIONAL -> listOf(clarityTemplate(), minimalProTemplate(), metroTemplate())
            TemplateCategory.CREATIVE -> listOf(artisanTemplate(), canvasTemplate())
            TemplateCategory.EXECUTIVE -> listOf(executiveSuiteTemplate(), boardroomTemplate())
            TemplateCategory.TECHNICAL -> listOf(codeMasterTemplate(), devProTemplate(), terminalTemplate())
            TemplateCategory.ALL -> getAllTemplates()
        }
    }
    
    // Modern Professional Templates
    
    private fun clarityTemplate() = TemplateConfig(
        id = "clarity",
        name = "Clarity",
        description = "Clean, professional single-column layout with excellent ATS compatibility",
        layout = LayoutType.SINGLE_COLUMN,
        colorScheme = ColorSchemes.professionalBlue(),
        isPremium = false,
        thumbnailUrl = null
    )
    
    private fun minimalProTemplate() = TemplateConfig(
        id = "minimal_pro",
        name = "Minimal Pro",
        description = "Minimalist design emphasizing content over decoration",
        layout = LayoutType.SINGLE_COLUMN,
        colorScheme = ColorSchemes.corporateGray(),
        isPremium = false,
        thumbnailUrl = null
    )
    
    private fun metroTemplate() = TemplateConfig(
        id = "metro",
        name = "Metro",
        description = "Modern design inspired by metro UI principles",
        layout = LayoutType.SINGLE_COLUMN,
        colorScheme = ColorSchemes.modernTeal(),
        isPremium = false,
        thumbnailUrl = null
    )
    
    // Creative Professional Templates
    
    private fun artisanTemplate() = TemplateConfig(
        id = "artisan",
        name = "Artisan",
        description = "Creative dual-column layout for designers and creatives",
        layout = LayoutType.DUAL_COLUMN,
        colorScheme = ColorSchemes.creativePurple(),
        isPremium = true,
        thumbnailUrl = null
    )
    
    private fun canvasTemplate() = TemplateConfig(
        id = "canvas",
        name = "Canvas",
        description = "Bold design for portfolios and creative professionals",
        layout = LayoutType.DUAL_COLUMN,
        colorScheme = ColorSchemes.warmOrange(),
        isPremium = true,
        thumbnailUrl = null
    )
    
    // Executive/Senior Templates
    
    private fun executiveSuiteTemplate() = TemplateConfig(
        id = "executive_suite",
        name = "Executive Suite",
        description = "Elegant design for C-level and senior executives",
        layout = LayoutType.SINGLE_COLUMN,
        colorScheme = ColorSchemes.executiveBlack(),
        isPremium = true,
        thumbnailUrl = null
    )
    
    private fun boardroomTemplate() = TemplateConfig(
        id = "boardroom",
        name = "Boardroom",
        description = "Professional design for senior management",
        layout = LayoutType.SINGLE_COLUMN,
        colorScheme = ColorSchemes.elegantBurgundy(),
        isPremium = true,
        thumbnailUrl = null
    )
    
    // Technical/Developer Templates
    
    private fun codeMasterTemplate() = TemplateConfig(
        id = "code_master",
        name = "CodeMaster",
        description = "Developer-focused single-column, optimized for technical roles",
        layout = LayoutType.SINGLE_COLUMN,
        colorScheme = ColorSchemes.professionalBlue(),
        isPremium = false,
        thumbnailUrl = null
    )
    
    private fun devProTemplate() = TemplateConfig(
        id = "dev_pro",
        name = "DevPro",
        description = "Technical resume with emphasis on skills and projects",
        layout = LayoutType.SINGLE_COLUMN,
        colorScheme = ColorSchemes.freshGreen(),
        isPremium = false,
        thumbnailUrl = null
    )
    
    private fun terminalTemplate() = TemplateConfig(
        id = "terminal",
        name = "Terminal",
        description = "Monospace-inspired design for software engineers",
        layout = LayoutType.SINGLE_COLUMN,
        colorScheme = ColorSchemes.corporateGray(),
        isPremium = true,
        thumbnailUrl = null
    )
}

enum class TemplateCategory {
    ALL,
    MODERN_PROFESSIONAL,
    CREATIVE,
    EXECUTIVE,
    TECHNICAL
}

// Extension to TemplateConfig
val TemplateConfig.category: TemplateCategory
    get() = when (id) {
        "clarity", "minimal_pro", "metro" -> TemplateCategory.MODERN_PROFESSIONAL
        "artisan", "canvas" -> TemplateCategory.CREATIVE
        "executive_suite", "boardroom" -> TemplateCategory.EXECUTIVE
        "code_master", "dev_pro", "terminal" -> TemplateCategory.TECHNICAL
        else -> TemplateCategory.MODERN_PROFESSIONAL
    }
