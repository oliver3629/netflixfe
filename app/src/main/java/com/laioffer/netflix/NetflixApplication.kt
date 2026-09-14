package com.laioffer.netflix

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Starts Hilt's app-level dependency graph.
@HiltAndroidApp
class NetflixApplication : Application()