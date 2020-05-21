package com.frankdroid7.farmerscenter.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FarmersData::class], version = 1, exportSchema = false)
public abstract class FarmersRoomDatabase : RoomDatabase() {

    abstract fun farmersDao(): FarmersDao

    companion object {

        @Volatile
        private var INSTANCE: FarmersRoomDatabase? = null

        fun getDatabase(context: Context): FarmersRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FarmersRoomDatabase::class.java,
                    "farmers_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}