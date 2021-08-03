package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.db.AsteroidDb
import com.udacity.asteroidradar.repository.PictureOfDayRepository
import kotlinx.coroutines.launch

class PictureOfDayVM(application: Application) : AndroidViewModel(application) {
    private val db = AsteroidDb.getInstance(application)
    private val repository = PictureOfDayRepository(db)
    private val _pictureOfDay = repository.getPictureOfDay().apply {
        viewModelScope.launch {
            repository.loadPictureOfDay()
            repository.removeOlderPictures()
        }
    }

    val pictureOfDay: LiveData<PictureOfDay?>
        get() = _pictureOfDay
}