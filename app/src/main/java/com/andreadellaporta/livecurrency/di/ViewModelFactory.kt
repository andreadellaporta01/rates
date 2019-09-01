package com.andreadellaporta.livecurrency.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.andreadellaporta.livecurrency.model.database.AppDatabase
import com.andreadellaporta.livecurrency.ui.RateListViewModel

class ViewModelFactory(private val activity: AppCompatActivity): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RateListViewModel::class.java)) {
            val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "currency").build()
            @Suppress("UNCHECKED_CAST")
            return RateListViewModel(db.rateDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}