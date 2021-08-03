package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.*
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.db.AsteroidDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PictureOfDayRepository(private val database: AsteroidDb) {

    fun getPictureOfDay(): LiveData<PictureOfDay?> {
        return database.pictureOfDayDao.get(getTodayDate()).map {
            it?.asPictureOfDay()
        }
    }

    suspend fun loadPictureOfDay() {
        withContext(Dispatchers.IO) { // Because is network process
            AsteroidApi.jsonService.getTodayPicture(getTodayDate(), Constants.API_KEY)
                .runCatching { // run catching to handle an exception that can happen in the process to load data
                    this.execute().body()?.let { pictureOfDay ->  //.let to prevent NPE
                        withContext(Dispatchers.Default) {
                            database.pictureOfDayDao.insert(pictureOfDay.asPictureOfDayEntity())
                        }
                    }
                }
        }
    }

    suspend fun removeOlderPictures() {
        withContext(Dispatchers.Default) { // Because db network process
            database.pictureOfDayDao.deleteOlder(getTodayDate())
        }
    }

}