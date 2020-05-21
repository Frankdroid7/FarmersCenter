package com.frankdroid7.farmerscenter.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FarmersDao {

    @Query("SELECT * from farmers_table")
    fun getAllFarmersData(): LiveData<List<FarmersData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(farmersData: FarmersData)

    @Query("SELECT * from farmers_table WHERE id= :id")
    fun getFarmersDataById(id: String): FarmersData

    @Query("DELETE FROM farmers_table")
    suspend fun deleteAll()
}