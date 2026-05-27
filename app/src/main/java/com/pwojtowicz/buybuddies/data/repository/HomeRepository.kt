package com.pwojtowicz.buybuddies.data.repository

import android.util.Log
import com.pwojtowicz.buybuddies.data.dao.HomeDao
import com.pwojtowicz.buybuddies.data.entity.Home
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val homeDao: HomeDao
) {
    suspend fun insertHome(home: Home) {
        try {
            homeDao.insert(home)
        } catch (e: Exception) {
            Log.e("HomeRepository", "Error inserting Home", e)
            throw e
        }
    }

    suspend fun deleteAll() {
        try {
            homeDao.deleteAll()
        } catch (e: Exception) {
            Log.e("HomeRepository", "Error deleting all homes", e)
            throw e
        }
    }

    fun getHomeById(id: String): Flow<Home?> = homeDao.getById(id)

    fun getAllHomes(): Flow<List<Home>> = homeDao.getAll()
}
