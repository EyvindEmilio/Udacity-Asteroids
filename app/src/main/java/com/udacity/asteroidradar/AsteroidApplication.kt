package com.udacity.asteroidradar

import android.app.Application
import com.udacity.asteroidradar.workers.FetchAsteroidWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AsteroidApplication : Application() {

    val applicationScope = CoroutineScope(Dispatchers.Default)

    private fun delayedInit() {
        applicationScope.launch {
            FetchAsteroidWorker.enqueueWorker()
        }
    }

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }
}