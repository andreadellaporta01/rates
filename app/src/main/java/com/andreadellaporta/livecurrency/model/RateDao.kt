package com.andreadellaporta.livecurrency.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RateDao {
    @get:Query("SELECT * FROM Rate")
    val all: MutableList<Rate>

    @Insert
    fun insertAll(rates: MutableList<Rate>)

    @Update
    fun updateAll(rates: MutableList<Rate>)
}