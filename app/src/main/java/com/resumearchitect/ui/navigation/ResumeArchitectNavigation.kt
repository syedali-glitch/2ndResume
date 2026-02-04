package com.resumearchitect.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.resumearchitect.ui.screens.home.HomeScreen
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Templates : Screen("templates")
    object Builder : Screen("builder/{resumeId}") {
        fun createRoute(resumeId: String) = "builder/$resumeId"
    }
    object Preview : Screen("preview/{resumeId}") {
        fun createRoute(resumeId: String) = "preview/$resumeId"
    }
    object ThemePreview : Screen("theme_preview/{resumeId}") {
        fun createRoute(resumeId: String) = "theme_preview/$resumeId"
    }
}

@Composable
fun ResumeArchitectNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(
            route = Screen.Builder.route,
            arguments = listOf(navArgument("resumeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val resumeId = backStackEntry.arguments?.getString("resumeId") ?: return@composable
            com.resumearchitect.ui.screens.builder.BuilderScreen(
                resumeId = resumeId,
                navController = navController
            )
        }
        
        composable(
            route = Screen.Preview.route,
            arguments = listOf(navArgument("resumeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val resumeId = backStackEntry.arguments?.getString("resumeId") ?: return@composable
            com.resumearchitect.ui.screens.preview.PreviewScreen(
                resumeId = resumeId,
                navController = navController
            )
        }
        
        composable(
            route = Screen.ThemePreview.route,
            arguments = listOf(navArgument("resumeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val resumeId = backStackEntry.arguments?.getString("resumeId") ?: return@composable
            
            if (resumeId == "new") {
                // Creation Mode: Use HomeViewModel to create a new resume
                val homeViewModel: com.resumearchitect.ui.screens.home.HomeViewModel = androidx.hilt.navigation.compose.hiltViewModel()
                val scope = androidx.compose.runtime.rememberCoroutineScope()
                
                com.resumearchitect.ui.screens.templates.ThemePreviewScreen(
                    resumeId = "",
                    navController = navController,
                    isNewResume = true,
                    onApplyTheme = { templateId, colorSchemeId ->
                        // Create new resume and navigate to builder
                        scope.launch {
                            val newResumeId = homeViewModel.createResumeForTheme(templateId, colorSchemeId)
                            // Replace current screen with builder to avoid back stack loop
                            navController.navigate(Screen.Builder.createRoute(newResumeId)) {
                                popUpTo(Screen.Home.route) { inclusive = false }
                            }
                        }
                    }
                )
            } else {
                // Edit Mode: Use PreviewViewModel to update existing resume
                val viewModel: com.resumearchitect.ui.screens.preview.PreviewViewModel = androidx.hilt.navigation.compose.hiltViewModel()
                
                com.resumearchitect.ui.screens.templates.ThemePreviewScreen(
                    resumeId = resumeId,
                    navController = navController,
                    isNewResume = false,
                    onApplyTheme = { templateId, colorSchemeId ->
                        viewModel.updateTheme(templateId, colorSchemeId)
                    }
                )
            }
        }
        
        composable(Screen.Templates.route) {
            com.resumearchitect.ui.screens.templates.TemplateSelectionScreen(
                resumeId = "", // Empty for browsing templates
                navController = navController
            )
        }
    }
}
