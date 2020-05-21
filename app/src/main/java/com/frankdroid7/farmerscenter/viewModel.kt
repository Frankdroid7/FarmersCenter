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

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FarmersRepository

    val allFarmersData: LiveData<List<FarmersData>>

    init {
        val wordsDao = FarmersRoomDatabase.getDatabase(application).farmersDao()
        repository = FarmersRepository(wordsDao)
        allFarmersData = repository.allFarmersData
    }

    fun insert(farmersData: FarmersData) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(farmersData)
    }
}