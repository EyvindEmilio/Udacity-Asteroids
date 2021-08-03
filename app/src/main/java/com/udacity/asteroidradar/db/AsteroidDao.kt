package com.udacity.asteroidradar.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asteroidEntity: AsteroidEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroidEntities: List<AsteroidEntity>)

    @Update
    suspend fun update(asteroidEntity: AsteroidEntity)

    @Query("SELECT * FROM asteroid WHERE id = :id")
    suspend fun get(id: Long): AsteroidEntity

    // We can use close_approach_date because uses the day of the asteroid
    @Query("SELECT * FROM asteroid WHERE close_approach_date = :date ORDER BY close_approach_date ASC")
    fun getByDate(date: String): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid WHERE close_approach_date >= :startDate AND close_approach_date <= :endDate ORDER BY close_approach_date ASC")
    fun getByDateRange(startDate: String, endDate: String): LiveData<List<AsteroidEntity>>

    @Query("DELETE FROM asteroid")
    suspend fun deleteAll()

    @Query("SELECT * FROM asteroid ORDER BY close_approach_date ASC")
    fun getAll(): LiveData<List<AsteroidEntity>>

    @Query("DELETE FROM asteroid WHERE close_approach_date < :date")
    suspend fun deleteAsteroidBeforeOf(date: String)
}