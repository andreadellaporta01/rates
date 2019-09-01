package com.andreadellaporta.livecurrency.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andreadellaporta.livecurrency.model.Rate
import com.andreadellaporta.livecurrency.model.RateDao

@Database(entities = [Rate::class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun rateDao() : RateDao

}