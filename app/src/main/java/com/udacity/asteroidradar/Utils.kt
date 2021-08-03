package com.udacity.asteroidradar

import android.os.Build
import androidx.annotation.RequiresApi
import com.udacity.asteroidradar.db.AsteroidEntity
import com.udacity.asteroidradar.db.PictureOfDayEntity
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
fun getTodayDate(): String {
    val format = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return format.format(Calendar.getInstance().time)
}

fun Asteroid.asAsteroidEntity() = AsteroidEntity(
    id = this.id,
    codename = this.codename,
    closeApproachDate = this.closeApproachDate,
    absoluteMagnitude = this.absoluteMagnitude,
    estimatedDiameter = this.estimatedDiameter,
    relativeVelocity = this.relativeVelocity,
    distanceFromEarth = this.distanceFromEarth,
    isPotentiallyHazardous = this.isPotentiallyHazardous
)

fun AsteroidEntity.asAsteroid() = Asteroid(
    id = this.id,
    codename = this.codename,
    closeApproachDate = this.closeApproachDate,
    absoluteMagnitude = this.absoluteMagnitude,
    estimatedDiameter = this.estimatedDiameter,
    relativeVelocity = this.relativeVelocity,
    distanceFromEarth = this.distanceFromEarth,
    isPotentiallyHazardous = this.isPotentiallyHazardous
)


fun PictureOfDay.asPictureOfDayEntity(): PictureOfDayEntity {
    return PictureOfDayEntity(
        date = this.date,
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}

fun PictureOfDayEntity.asPictureOfDay(): PictureOfDay {
    return PictureOfDay(
        date = this.date,
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}

fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}