package com.aimusicplayer.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aimusicplayer.app.presentation.screens.*
import com.aimusicplayer.app.service.MusicPlayerService

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home")
    object Search : Screen("search")
    object Library : Screen("library")
    object Player : Screen("player")
    object Lyrics : Screen("lyrics")
    object Playlist : Screen("playlist/{playlistId}") {
        fun createRoute(id: String) = "playlist/$id"
    }
    object Settings : Screen("settings")
    object Admin : Screen("admin")
}

@Composable
fun NavGraph(
    navController: NavHostController = androidx.navigation.compose.rememberNavController(),
    musicService: MusicPlayerService?
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Signup.route) {
            SignupScreen(navController = navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController, musicService = musicService)
        }
        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(Screen.Library.route) {
            LibraryScreen(navController = navController)
        }
        composable(Screen.Player.route) {
            PlayerScreen(navController = navController, musicService = musicService)
        }
        composable(Screen.Lyrics.route) {
            LyricsScreen(navController = navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable(Screen.Admin.route) {
            AdminScreen(navController = navController)
        }
    }
}
