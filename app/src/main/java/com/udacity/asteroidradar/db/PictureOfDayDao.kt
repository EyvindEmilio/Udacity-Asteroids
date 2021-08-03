package com.udacity.asteroidradar.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PictureOfDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pictureOfDayEntity: PictureOfDayEntity)

    @Query("SELECT * FROM picture_of_day WHERE date = :date")
    fun get(date: String): LiveData<PictureOfDayEntity?>

    @Query("DELETE FROM picture_of_day WHERE date != :currentDate")
    suspend fun deleteOlder(currentDate: String)

}