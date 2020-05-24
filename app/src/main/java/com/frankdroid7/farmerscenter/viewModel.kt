package com.frankdroid7.farmerscenter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.frankdroid7.farmerscenter.database.FarmersData
import com.frankdroid7.farmerscenter.database.FarmersRepository
import com.frankdroid7.farmerscenter.database.FarmersRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FarmersViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FarmersRepository

    var allFarmersData: LiveData<MutableList<FarmersData>>


    init {
        val wordsDao = FarmersRoomDatabase.getDatabase(application).farmersDao()
        repository = FarmersRepository(wordsDao)
        allFarmersData = repository.allFarmersData
    }

    fun insert(farmersData: FarmersData) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(farmersData)
    }

    fun deleteFarmersDataById(id: Int){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteFarmersDataById(id)
        }
    }

    fun deleteAllRecords(){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAllRecords()

        }
    }
}