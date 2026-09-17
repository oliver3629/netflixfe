package com.laioffer.netflix.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.snapshots.SnapshotId
import androidx.compose.ui.graphics.vector.ImageVector
import com.laioffer.netflix.R

// Groups the app's route definitions in one place.
sealed class Screen(val route: String) {
    companion object {
        const val ARG_VIDEO_ID = "videoId"
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

    // Detail route receives the selected backend video id.
    object VideoDetail : Screen("detail/{$ARG_VIDEO_ID}") {
        fun createRoute(videoId: String): String = "detail/$videoId"
    }
}