package com.frankdroid7.farmerscenter.database

import androidx.lifecycle.LiveData

class FarmersRepository(private val farmersDao: FarmersDao) {

    val allFarmersData: LiveData<MutableList<FarmersData>> = farmersDao.getAllFarmersData()

    suspend fun insert(farmersData: FarmersData) {
        farmersDao.insert(farmersData)
    }
    suspend fun getFarmersDataById(id: Int): FarmersData{
        return farmersDao.getFarmersDataById(id)
    }

    suspend fun deleteFarmersDataById(id: Int){
        farmersDao.deleteFarmersDataById(id)
    }
    suspend fun deleteAllRecords(){
        farmersDao.deleteAll()
    }
}