package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.db.AsteroidDb
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class AsteroidApiStatus { LOADING, ERROR, DONE }
enum class AsteroidInterval { WEEK, TODAY, SAVED }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AsteroidDb.getInstance(application)
    private val repository = AsteroidRepository(db)

    private val _status = MutableLiveData<AsteroidApiStatus>()
    val status: LiveData<AsteroidApiStatus>
        get() = _status

    private val _interval = MutableLiveData<AsteroidInterval>()

    private val _asteroids = Transformations.switchMap(_interval) {
        val liveData = when (it) {
            AsteroidInterval.WEEK -> repository.getWeekAsteroids()
            AsteroidInterval.TODAY -> repository.getTodayAsteroids()
            AsteroidInterval.SAVED -> repository.getSavedAsteroids()
            else -> repository.getTodayAsteroids() // Load toad by default
        }
        fetchAsteroids()
        liveData.map { list ->
            if (list.isNotEmpty()) {
                _status.value = AsteroidApiStatus.DONE // set done if there is data saved in DB
            }
            list
        }
    }
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    init {
        _interval.value = AsteroidInterval.TODAY
    }

    fun setInterval(interval: AsteroidInterval) {
        this._interval.value = interval
    }

    private fun fetchAsteroids() {
        _status.value = AsteroidApiStatus.LOADING

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.loadAsteroids()
                withContext(Dispatchers.Main) {
                    _status.value = AsteroidApiStatus.DONE
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _status.value = AsteroidApiStatus.ERROR
                }
            }
        }
    }

}