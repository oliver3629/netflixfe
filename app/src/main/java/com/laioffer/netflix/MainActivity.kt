package com.laioffer.netflix

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.laioffer.netflix.navigation.NetflixNavHost
import com.laioffer.netflix.navigation.Screen
import com.laioffer.netflix.network.NetworkModuleNoDi
import com.laioffer.netflix.ui.components.BottomNavigationBar
import com.laioffer.netflix.ui.theme.NetflixTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MainActivity"
private const val NETWORK_TAG = "Network"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        enableEdgeToEdge()
        logHomeResponse()
        setContent {
            NetflixTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Creates one NavController instance for this activity's Compose tree.
                    val navController = rememberNavController()

                    // Watches the back stack so Compose can react when the destination changes.
                    val navBackStackEntry by navController.currentBackStackEntryAsState()

                    // Reads the current route, such as "home" or "profile".
                    val currentRoute = navBackStackEntry?.destination?.route

                    // Lists the top-level tabs that should appear in the bottom bar.
                    val bottomBarScreens = listOf(
                        Screen.BottomBarScreen.Home,
                        Screen.BottomBarScreen.Profile
                    )

                    // Hides the bottom bar for future non-tab routes like detail pages.
                    val isTopLevelRoute = bottomBarScreens.any { it.route == currentRoute }

                    Scaffold(
                        bottomBar = {
                            if (isTopLevelRoute) {
                                BottomNavigationBar(
                                    screens = bottomBarScreens,
                                    currentRoute = currentRoute,
                                    navController = navController
                                )
                            }
                        }
                    ) { paddingValues ->
                        NetflixNavHost(
                            navController = navController,
                            modifier = Modifier.padding(paddingValues)
                        )
                    }
                }
            }
        }

    }

    // Calls the backend once so Logcat proves Retrofit is connected.
    private fun logHomeResponse() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    NetworkModuleNoDi.api.getHome().execute()
                }

                if (response.isSuccessful) {
                    Log.d(NETWORK_TAG, "Home response: ${response.body()}")
                } else {
                    Log.e(NETWORK_TAG, "Home request failed: ${response.code()}")
                }
            } catch (t: Throwable) {
                Log.e(NETWORK_TAG, "Home request error", t)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}

@Composable
fun MainScreen(onOpenPractice: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        Text(
            text = "Netflix Template App",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(text = "Activity: MainActivity")

        Button(onClick = onOpenPractice) {
            Text(text = "Open Compose Practice")
        }
    }


}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NetflixTheme {
        Greeting("Android")
    }
}