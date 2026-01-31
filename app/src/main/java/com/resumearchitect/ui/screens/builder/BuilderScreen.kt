package com.resumearchitect.ui.screens.builder

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.resumearchitect.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuilderScreen(
    resumeId: String,
    navController: NavController,
    viewModel: BuilderViewModel = hiltViewModel()
) {
    val resume by viewModel.resume.collectAsState()
    val currentSection by viewModel.currentSection.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    
    Scaffold(
        topBar = {
            BuilderTopBar(
                title = resume?.title ?: "Resume",
                onBackClick = { navController.navigateUp() },
                onPreviewClick = {
                    navController.navigate(
                        com.resumearchitect.ui.navigation.Screen.Preview.createRoute(resumeId)
                    )
                },
                isSaving = isSaving
            )
        },
        bottomBar = {
            BuilderBottomNavigation(
                currentSection = currentSection,
                onSectionSelected = viewModel::navigateToSection
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentSection) {
                BuilderSection.CONTACT -> ContactInfoScreen(viewModel)
                BuilderSection.SUMMARY -> SummaryScreen(viewModel)
                BuilderSection.EXPERIENCE -> ExperienceScreen(viewModel)
                BuilderSection.EDUCATION -> EducationScreen(viewModel)
                BuilderSection.SKILLS -> SkillsScreen(viewModel)
                BuilderSection.CUSTOM -> CustomSectionsScreen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuilderTopBar(
    title: String,
    onBackClick: () -> Unit,
    onPreviewClick: () -> Unit,
    isSaving: Boolean
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        cornerRadius = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                GlassIconButton(onClick = onBackClick) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    if (isSaving) {
                        Text(
                            text = "Saving...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            GlassIconButton(onClick = onPreviewClick) {
                Icon(
                    Icons.Default.Visibility,
                    contentDescription = "Preview",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun BuilderBottomNavigation(
    currentSection: BuilderSection,
    onSectionSelected: (BuilderSection) -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 0.dp
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f)
        ) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                label = { Text("Contact") },
                selected = currentSection == BuilderSection.CONTACT,
                onClick = { onSectionSelected(BuilderSection.CONTACT) }
            )
            
            NavigationBarItem(
                icon = { Icon(Icons.Default.Work, contentDescription = null) },
                label = { Text("Experience") },
                selected = currentSection == BuilderSection.EXPERIENCE,
                onClick = { onSectionSelected(BuilderSection.EXPERIENCE) }
            )
            
            NavigationBarItem(
                icon = { Icon(Icons.Default.School, contentDescription = null) },
                label = { Text("Education") },
                selected = currentSection == BuilderSection.EDUCATION,
                onClick = { onSectionSelected(BuilderSection.EDUCATION) }
            )
            
            NavigationBarItem(
                icon = { Icon(Icons.Default.Star, contentDescription = null) },
                label = { Text("Skills") },
                selected = currentSection == BuilderSection.SKILLS,
                onClick = { onSectionSelected(BuilderSection.SKILLS) }
            )
        }
    }
}

// Placeholder screens - to be implemented
@Composable
fun SummaryScreen(viewModel: BuilderViewModel) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Summary Editor - Coming Soon")
    }
}

@Composable
fun CustomSectionsScreen(viewModel: BuilderViewModel) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Custom Sections - Coming Soon")
    }
}
