package com.resumearchitect.ui.screens.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resumearchitect.data.models.Resume
import com.resumearchitect.templates.ColorSchemes

object ThemePreviewLayouts {

    @Composable
    fun ModernLayout(
        colorScheme: ColorSchemes.ColorSchemeConfig,
        data: PreviewData
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Header with primary color
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(colorScheme.primaryColor))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = data.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = data.role,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${data.email} • ${data.phone}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            // Content
            Column(modifier = Modifier.padding(16.dp)) {
                Section(title = "Summary", accentColor = Color(colorScheme.accentColor)) {
                    Text(data.summary, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Section(title = "Experience", accentColor = Color(colorScheme.accentColor)) {
                    Text(data.company, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                    Text(data.dates, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                    Text(data.description, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Section(title = "Education", accentColor = Color(colorScheme.accentColor)) {
                    Text(data.university, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                    Text(data.degree, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Section(title = "Skills", accentColor = Color(colorScheme.accentColor)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        data.skills.forEach { skill ->
                            Surface(
                                color = Color(colorScheme.accentColor).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = skill,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(colorScheme.accentColor)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SidebarLayout(
        colorScheme: ColorSchemes.ColorSchemeConfig,
        data: PreviewData
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Sidebar
            Column(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .background(Color(colorScheme.primaryColor))
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Circle Initials
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = data.name.take(1),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Contact
                Text("CONTACT", color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(data.email, color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelSmall)
                Text(data.phone, color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelSmall)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Skills
                Text("SKILLS", color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                data.skills.take(3).forEach {
                    Text("• $it", color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.labelSmall)
                }
            }

            // Main Content
            Column(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                Text(
                    text = data.name.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(colorScheme.primaryColor),
                    letterSpacing = 2.sp
                )
                Text(
                    text = data.role,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                
                Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(colorScheme.accentColor))
                
                Text("PROFILE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(colorScheme.accentColor))
                Text(data.summary, style = MaterialTheme.typography.bodySmall, maxLines = 4)
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text("EXPERIENCE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(colorScheme.accentColor))
                Text(data.company, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                Text(data.dates, color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                Text(data.description, style = MaterialTheme.typography.bodySmall, maxLines = 2)
            }
        }
    }

    @Composable
    fun ClassicLayout(
        colorScheme: ColorSchemes.ColorSchemeConfig,
        data: PreviewData
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(24.dp)
        ) {
            // Header - Center aligned, serif look attempted
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = data.name.uppercase(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${data.email} | ${data.phone} | New York, NY",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            
            Divider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color.Black)
            
            // Sections
            Column {
                ClassicSection("SUMMARY") {
                    Text(data.summary, style = MaterialTheme.typography.bodySmall)
                }
                
                ClassicSection("EXPERIENCE") {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(data.company, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                        Text(data.dates, style = MaterialTheme.typography.bodySmall)
                    }
                    Text(data.role, style = MaterialTheme.typography.bodySmall, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    Text("• ${data.description}", style = MaterialTheme.typography.bodySmall)
                }
                
                ClassicSection("EDUCATION") {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(data.university, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                        Text("Graduated 2018", style = MaterialTheme.typography.bodySmall)
                    }
                    Text(data.degree, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }

    @Composable
    private fun Section(title: String, accentColor: Color, content: @Composable () -> Unit) {
        Column {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            content()
        }
    }
    
    @Composable
    private fun ClassicSection(title: String, content: @Composable () -> Unit) {
        Column(modifier = Modifier.padding(bottom = 12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            content()
        }
    }

    data class PreviewData(
        val name: String,
        val role: String,
        val email: String,
        val phone: String,
        val summary: String,
        val company: String,
        val dates: String,
        val description: String,
        val university: String,
        val degree: String,
        val skills: List<String>
    )
}
