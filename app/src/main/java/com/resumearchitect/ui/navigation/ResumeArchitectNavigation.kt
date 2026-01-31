package com.resumearchitect.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.resumearchitect.ui.screens.home.HomeScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Templates : Screen("templates")
    object Builder : Screen("builder/{resumeId}") {
        fun createRoute(resumeId: String) = "builder/$resumeId"
    }
    object Preview : Screen("preview/{resumeId}") {
        fun createRoute(resumeId: String) = "preview/$resumeId"
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
        
        // TODO: Add template selection screen
        // composable(Screen.Templates.route) { TemplateSelectionScreen(navController) }
    }
}
