package com.frankdroid7.farmerscenter.database

import androidx.lifecycle.LiveData

class FarmersRepository(private val farmersDao: FarmersDao) {

    val allFarmersData: LiveData<List<FarmersData>> = farmersDao.getAllFarmersData()

    suspend fun insert(farmersData: FarmersData) {
        farmersDao.insert(farmersData)
    }
}