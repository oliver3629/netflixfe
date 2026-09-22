package com.laioffer.netflix.navigation

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.snapshots.SnapshotId
import androidx.compose.ui.graphics.vector.ImageVector
import com.laioffer.netflix.R

// Describes the routes that Navigation Compose can display.
sealed class Screen(val route: String) {
    companion object {
        const val ARG_VIDEO_ID = "videoId"
        const val ARG_VIDEO_URL = "videoUrl"
    }

    sealed class BottomBarScreen(
        route: String,
        val titleResId: Int,
        val icon: ImageVector
    ) : Screen(route) {
        object Home : BottomBarScreen(
            route = "home",
            titleResId = R.string.home,
            icon = Icons.Default.Home
        )

        object Profile : BottomBarScreen(
            route = "profile",
            titleResId = R.string.my_netflix,
            icon = Icons.Default.Person
        )
    }

    object VideoDetail : Screen("detail/{$ARG_VIDEO_ID}") {
        fun createRoute(videoId: String): String = "detail/$videoId"
    }

    object Player : Screen("player/{$ARG_VIDEO_URL}") {
        fun createRoute(videoUrl: String): String = "player/${Uri.encode(videoUrl, "")}"
    }
}