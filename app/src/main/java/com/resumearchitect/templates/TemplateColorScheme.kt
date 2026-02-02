package com.resumearchitect.templates

import androidx.compose.ui.graphics.Color

/**
 * Color scheme for resume templates
 * Each template can have multiple color variations
 */
data class TemplateColorScheme(
    val id: String,
    val name: String,
    val primary: Color,
    val secondary: Color,
    val accent: Color,
    val background: Color = Color.White,
    val text: Color = Color(0xFF1A1A1A)
)

/**
 * Predefined color schemes for templates
 */
object ColorSchemes {
    
    val PROFESSIONAL_BLUE = TemplateColorScheme(
        id = "professional_blue",
        name = "Professional Blue",
        primary = Color(0xFF0066CC),
        secondary = Color(0xFF004D99),
        accent = Color(0xFF0080FF)
    )
    
    val ELEGANT_PURPLE = TemplateColorScheme(
        id = "elegant_purple",
        name = "Elegant Purple",
        primary = Color(0xFF7B2CBF),
        secondary = Color(0xFF5A189A),
        accent = Color(0xFF9D4EDD)
    )
    
    val CREATIVE_ORANGE = TemplateColorScheme(
        id = "creative_orange",
        name = "Creative Orange",
        primary = Color(0xFFFF6B35),
        secondary = Color(0xFFE55934),
        accent = Color(0xFFFF8C42)
    )
    
    val MODERN_TEAL = TemplateColorScheme(
        id = "modern_teal",
        name = "Modern Teal",
        primary = Color(0xFF00B4D8),
        secondary = Color(0xFF0096C7),
        accent = Color(0xFF48CAE4)
    )
    
    val BOLD_RED = TemplateColorScheme(
        id = "bold_red",
        name = "Bold Red",
        primary = Color(0xFFDC2626),
        secondary = Color(0xFFB91C1C),
        accent = Color(0xFFEF4444)
    )
    
    val SOPHISTICATED_GRAY = TemplateColorScheme(
        id = "sophisticated_gray",
        name = "Sophisticated Gray",
        primary = Color(0xFF4B5563),
        secondary = Color(0xFF374151),
        accent = Color(0xFF6B7280)
    )
    
    val FRESH_GREEN = TemplateColorScheme(
        id = "fresh_green",
        name = "Fresh Green",
        primary = Color(0xFF10B981),
        secondary = Color(0xFF059669),
        accent = Color(0xFF34D399)
    )
    
    val CLASSIC_BLACK = TemplateColorScheme(
        id = "classic_black",
        name = "Classic Black",
        primary = Color(0xFF000000),
        secondary = Color(0xFF1F1F1F),
        accent = Color(0xFF404040)
    )
    
    /**
     * All available color schemes
     */
    val ALL = listOf(
        PROFESSIONAL_BLUE,
        ELEGANT_PURPLE,
        CREATIVE_ORANGE,
        MODERN_TEAL,
        BOLD_RED,
        SOPHISTICATED_GRAY,
        FRESH_GREEN,
        CLASSIC_BLACK
    )
    
    /**
     * Get color scheme by ID
     */
    fun getById(id: String): TemplateColorScheme? {
        return ALL.find { it.id == id }
    }
}
