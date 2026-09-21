package com.laioffer.netflix

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
import com.laioffer.netflix.database.FavoriteDao
import com.laioffer.netflix.database.VideoEntity
import com.laioffer.netflix.navigation.NetflixNavHost
import com.laioffer.netflix.navigation.Screen
import com.laioffer.netflix.network.NetworkApi
import com.laioffer.netflix.ui.components.BottomNavigationBar
import com.laioffer.netflix.ui.theme.NetflixTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "MainActivity"
private const val NETWORK_TAG = "Network"
private const val DATABASE_TAG = "Database"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkApi: NetworkApi

    @Inject
    lateinit var favoriteDao: FavoriteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        logHomeResponse()
        // testFavoriteDatabaseSetup()
        enableEdgeToEdge()
        setContent {
            NetflixTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val bottomBarScreens = listOf(
                        Screen.BottomBarScreen.Home,
                        Screen.BottomBarScreen.Profile
                    )
                    val navBackStackEntry by navController.currentBackStackEntryAsState()

                    // Reads the current route, such as "home" or "profile".
                    val currentRoute = navBackStackEntry?.destination?.route
                    val isTopLevelRoute = bottomBarScreens.any { it.route == currentRoute }

//                    Column(
//                        modifier = Modifier.fillMaxSize()
//                    ) {
//                        NetflixNavHost(navController = navController)
//                        BottomNavigationBar(
//                            bottomBarScreens,
//                            currentRoute,
//                            navController
//                        )
//                    }

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

    private fun logHomeResponse() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    networkApi.getHome().execute()
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

    private fun testFavoriteDatabaseSetup() {
        lifecycleScope.launch {
            try {
                favoriteDao.insert(
                    VideoEntity(
                        id = "kpop-demon-hunters",
                        title = "KPop Demon Hunters",
                        posterUrl = ""
                    )
                )
            } catch (t: Throwable) {
                Log.e(DATABASE_TAG, "Favorite database test failed", t)
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