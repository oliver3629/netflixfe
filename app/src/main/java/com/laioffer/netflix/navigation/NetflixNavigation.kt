package com.laioffer.netflix.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.laioffer.netflix.player.VideoPlayerScreen
import com.laioffer.netflix.ui.home.HomeScreen
import com.laioffer.netflix.ui.profile.ProfileScreen
import com.laioffer.netflix.ui.videodetail.VideoDetailScreen

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
            HomeScreen(
                onVideoClick = { videoId -> navController.navigate(Screen.VideoDetail.createRoute(videoId))}
            )
        }

        composable(
            route = Screen.VideoDetail.route,
            arguments = listOf(
                navArgument(Screen.ARG_VIDEO_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry -> val videoId = backStackEntry.arguments?.getString(Screen.ARG_VIDEO_ID) ?: return@composable
            VideoDetailScreen(
                videoId = videoId,
                onBackClick = {
                    navController.popBackStack()
                },
                onPlayClick = { videoUrl ->
                    Log.d("Playback", "Play clicked: $videoUrl")
                    navController.navigate(Screen.Player.createRoute(videoUrl))
                }
            )
        }

        composable(Screen.BottomBarScreen.Profile.route) {
            ProfileScreen(
                onVideoClick = { videoId ->
                    navController.navigate(Screen.VideoDetail.createRoute(videoId))
                }
            )
        }

        composable(
            route = Screen.Player.route,
            arguments = listOf(
                navArgument(Screen.ARG_VIDEO_URL) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val videoUrl = backStackEntry.arguments
                ?.getString(Screen.ARG_VIDEO_URL)
                ?.let { Uri.decode(it) }

            if (videoUrl.isNullOrBlank()) {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
                return@composable
            }

            VideoPlayerScreen(
                videoUrl = videoUrl
            )
        }
    }
}