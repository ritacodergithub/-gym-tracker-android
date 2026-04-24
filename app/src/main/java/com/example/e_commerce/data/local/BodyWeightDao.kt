package com.example.e_commerce.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BodyWeightDao {

    @Query("SELECT * FROM body_weights ORDER BY measuredAt DESC")
    fun observeAll(): Flow<List<BodyWeightEntity>>

    @Query("SELECT * FROM body_weights ORDER BY measuredAt DESC LIMIT 1")
    fun observeLatest(): Flow<BodyWeightEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: BodyWeightEntity): Long

    @Delete
    suspend fun delete(entry: BodyWeightEntity)

    @Query("DELETE FROM body_weights")
    suspend fun clearAll()
}