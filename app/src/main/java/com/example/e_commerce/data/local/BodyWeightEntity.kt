package com.example.e_commerce.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Daily body weight log. One row per measurement.
@Entity(tableName = "body_weights")
data class BodyWeightEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weightKg: Float,
    val measuredAt: Long = System.currentTimeMillis()
)