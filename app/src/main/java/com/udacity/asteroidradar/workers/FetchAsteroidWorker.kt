package com.udacity.asteroidradar.workers

import android.content.Context
import androidx.work.*
import com.udacity.asteroidradar.db.AsteroidDb
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class FetchAsteroidWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val WORK_NAME = "FETCH_ASTEROIDS_WORKER"

        /**
         * Enqueue worker to run every day if the device is charging and it's connected to WIFI
         */
        fun enqueueWorker() {
            val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()

            val workerRequest = PeriodicWorkRequestBuilder<FetchAsteroidWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance()
                .enqueueUniquePeriodicWork(
                    WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    workerRequest
                )
        }
    }

    override suspend fun doWork(): Result {
        val database = AsteroidDb.getInstance(applicationContext)
        val repository = AsteroidRepository(database)
        return try {
            repository.loadAsteroids()
            repository.clearLastAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}