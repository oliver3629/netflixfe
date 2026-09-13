package com.laioffer.netflix.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.laioffer.netflix.navigation.Screen

// Renders the top-level tabs and asks NavController to switch destinations.
@Composable
fun BottomNavigationBar(
    screens: List<Screen.BottomBarScreen>,
    currentRoute: String?,
    navController: NavHostController
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        screens.forEach { screen ->
            val title = stringResource(screen.titleResId)

            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        // Navigate like a tab bar: keep one copy of each top-level screen
                        // and restore its prior state when the user comes back to it.
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                label = {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        }
    }
}