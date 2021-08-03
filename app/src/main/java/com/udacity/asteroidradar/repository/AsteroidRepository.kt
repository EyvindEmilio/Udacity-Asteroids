package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.*
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.db.AsteroidDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDb) {

    fun getTodayAsteroids(): LiveData<List<Asteroid>> {
        val todayDate = getTodayDate()
        return database.asteroidDao.getByDate(todayDate).map { list ->
            list.map { it.asAsteroid() }
        }
    }

    fun getWeekAsteroids(): LiveData<List<Asteroid>> {
        val dates = getNextSevenDaysFormattedDates()
        val startDate = dates.first()
        val endDate = dates.last()

        return database.asteroidDao.getByDateRange(startDate, endDate).map { list ->
            list.map { it.asAsteroid() }
        }
    }

    fun getSavedAsteroids(): LiveData<List<Asteroid>> {
        return database.asteroidDao.getAll().map { list ->
            list.map { it.asAsteroid() }
        }
    }

    /**
     * Load Asteroids of the next 7 days
     */
    suspend fun loadAsteroids() {
        val startDate = getTodayDate()
        withContext(Dispatchers.IO) {
            AsteroidApi.scalarService.getFeed(startDate, Constants.API_KEY)
                .runCatching {
                    this.execute().body()?.let { contentResponse ->
                        val asteroids = parseAsteroidsJsonResult(JSONObject(contentResponse))
                            .map { it.asAsteroidEntity() }
                        withContext(Dispatchers.Default) { database.asteroidDao.insertAll(asteroids) }
                    }
                }
        }
    }

    /**
     * Delete all past asteroids
     */
    suspend fun clearLastAsteroids() {
        val todayDate = getTodayDate()
        withContext(Dispatchers.Default) {
            database.asteroidDao.deleteAsteroidBeforeOf(todayDate)
        }
    }

}