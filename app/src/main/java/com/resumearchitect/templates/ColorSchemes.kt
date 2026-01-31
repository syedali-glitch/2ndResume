package com.resumearchitect.templates

import android.graphics.Color
import com.resumearchitect.pdf.ColorScheme

/**
 * Predefined color schemes for resume templates
 * 8 professional color palettes
 */
object ColorSchemes {
    
    /**
     * Professional Blue - Trust, reliability, corporate
     * Perfect for: Finance, IT, Corporate roles
     */
    fun professionalBlue() = ColorScheme(
        primaryColor = Color.rgb(99, 102, 241),    // Indigo
        secondaryColor = Color.rgb(107, 114, 128),  // Gray-500
        textColor = Color.rgb(17, 24, 39),          // Gray-900
        accentColor = Color.rgb(59, 130, 246)       // Blue-500
    )
    
    /**
     * Corporate Gray - Professional, sophisticated
     * Perfect for: Law, Consulting, Executive roles
     */
    fun corporateGray() = ColorScheme(
        primaryColor = Color.rgb(55, 65, 81),       // Gray-700
        secondaryColor = Color.rgb(107, 114, 128),  // Gray-500
        textColor = Color.rgb(17, 24, 39),          // Gray-900
        accentColor = Color.rgb(75, 85, 99)         // Gray-600
    )
    
    /**
     * Modern Teal - Fresh, innovative, tech-forward
     * Perfect for: Startups, Tech, Innovation roles
     */
    fun modernTeal() = ColorScheme(
        primaryColor = Color.rgb(6, 182, 212),      // Cyan-500
        secondaryColor = Color.rgb(107, 114, 128),  // Gray-500
        textColor = Color.rgb(17, 24, 39),          // Gray-900
        accentColor = Color.rgb(20, 184, 166)       // Teal-500
    )
    
    /**
     * Creative Purple - Artistic, imaginative
     * Perfect for: Design, Creative, Marketing roles
     */
    fun creativePurple() = ColorScheme(
        primaryColor = Color.rgb(139, 92, 246),     // Purple-500
        secondaryColor = Color.rgb(107, 114, 128),  // Gray-500
        textColor = Color.rgb(17, 24, 39),          // Gray-900
        accentColor = Color.rgb(168, 85, 247)       // Purple-400
    )
    
    /**
     * Warm Orange - Energetic, approachable
     * Perfect for: Sales, Customer Service, Education
     */
    fun warmOrange() = ColorScheme(
        primaryColor = Color.rgb(249, 115, 22),     // Orange-500
        secondaryColor = Color.rgb(107, 114, 128),  // Gray-500
        textColor = Color.rgb(17, 24, 39),          // Gray-900
        accentColor = Color.rgb(251, 146, 60)       // Orange-400
    )
    
    /**
     * Executive Black - Powerful, authoritative
     * Perfect for: C-Level, Senior Management, Executive roles
     */
    fun executiveBlack() = ColorScheme(
        primaryColor = Color.rgb(17, 24, 39),       // Gray-900
        secondaryColor = Color.rgb(75, 85, 99),     // Gray-600
        textColor = Color.rgb(17, 24, 39),          // Gray-900
        accentColor = Color.rgb(99, 102, 241)       // Indigo-500 (accent)
    )
    
    /**
     * Fresh Green - Growth, sustainability
     * Perfect for: Environmental, Healthcare, Education
     */
    fun freshGreen() = ColorScheme(
        primaryColor = Color.rgb(34, 197, 94),      // Green-500
        secondaryColor = Color.rgb(107, 114, 128),  // Gray-500
        textColor = Color.rgb(17, 24, 39),          // Gray-900
        accentColor = Color.rgb(74, 222, 128)       // Green-400
    )
    
    /**
     * Elegant Burgundy - Sophisticated, refined
     * Perfect for: Luxury, Wine, Hospitality, Senior roles
     */
    fun elegantBurgundy() = ColorScheme(
        primaryColor = Color.rgb(136, 19, 55),      // Rose-900
        secondaryColor = Color.rgb(107, 114, 128),  // Gray-500
        textColor = Color.rgb(17, 24, 39),          // Gray-900
        accentColor = Color.rgb(190, 18, 60)        // Rose-700
    )
    
    /**
     * Get all available color schemes
     */
    fun getAllSchemes(): List<Pair<String, ColorScheme>> {
        return listOf(
            "Professional Blue" to professionalBlue(),
            "Corporate Gray" to corporateGray(),
            "Modern Teal" to modernTeal(),
            "Creative Purple" to creativePurple(),
            "Warm Orange" to warmOrange(),
            "Executive Black" to executiveBlack(),
            "Fresh Green" to freshGreen(),
            "Elegant Burgundy" to elegantBurgundy()
        )
    }
    
    /**
     * Get color scheme by name
     */
    fun getSchemeByName(name: String): ColorScheme {
        return when (name.lowercase()) {
            "professional blue", "blue" -> professionalBlue()
            "corporate gray", "gray" -> corporateGray()
            "modern teal", "teal" -> modernTeal()
            "creative purple", "purple" -> creativePurple()
            "warm orange", "orange" -> warmOrange()
            "executive black", "black" -> executiveBlack()
            "fresh green", "green" -> freshGreen()
            "elegant burgundy", "burgundy" -> elegantBurgundy()
            else -> professionalBlue() // Default
        }
    }
}
