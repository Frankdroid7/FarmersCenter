package com.frankdroid7.farmerscenter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "farmers_table")
class FarmersData(@PrimaryKey(autoGenerate = true) val id: Int,
                  @ColumnInfo val farmers_name: String,
                  @ColumnInfo val farmers_age: String,
                  @ColumnInfo val farmers_image: String,
                  @ColumnInfo val farm_name: String,
                  @ColumnInfo val farm_location: String,
                  @ColumnInfo val farm_lat1: Double,
                  @ColumnInfo val farm_lon1:Double,
                  @ColumnInfo val farm_lat2:Double,
                  @ColumnInfo val farm_lon2:Double,
                  @ColumnInfo val farm_lat3:Double,
                  @ColumnInfo val farm_lon3:Double,
                  @ColumnInfo val farm_lat4:Double,
                  @ColumnInfo val farm_lon4:Double)