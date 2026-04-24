package com.example.e_commerce.data.repository

import com.example.e_commerce.data.local.BodyWeightDao
import com.example.e_commerce.data.local.BodyWeightEntity
import kotlinx.coroutines.flow.Flow

class BodyWeightRepository(private val dao: BodyWeightDao) {

    fun observeAll(): Flow<List<BodyWeightEntity>> = dao.observeAll()

    fun observeLatest(): Flow<BodyWeightEntity?> = dao.observeLatest()

    suspend fun log(weightKg: Float, measuredAt: Long = System.currentTimeMillis()) {
        dao.insert(BodyWeightEntity(weightKg = weightKg, measuredAt = measuredAt))
    }

    suspend fun delete(entry: BodyWeightEntity) = dao.delete(entry)

    suspend fun clear() = dao.clearAll()
}