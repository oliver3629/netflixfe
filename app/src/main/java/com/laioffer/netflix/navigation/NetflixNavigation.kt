package com.laioffer.netflix.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.laioffer.netflix.ui.home.HomeScreen
import com.laioffer.netflix.ui.profile.ProfileScreen

// Connects route names to composable destinations.
@Composable
fun NetflixNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.BottomBarScreen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.BottomBarScreen.Home.route) {
            HomeScreen()
        }

        composable(Screen.BottomBarScreen.Profile.route) {
            ProfileScreen()
        }
    }
}