package com.frankdroid7.farmerscenter.database

import androidx.lifecycle.LiveData

class FarmersRepository(private val wordDao: FarmersDao) {

    val allFarmersData: LiveData<List<FarmersData>> = wordDao.getAllFarmersData()

    suspend fun insert(farmersData: FarmersData) {
        wordDao.insert(farmersData)
    }
}