package com.andreadellaporta.livecurrency.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rate(
    @field: PrimaryKey
    val country: String,
    val value: Double
)