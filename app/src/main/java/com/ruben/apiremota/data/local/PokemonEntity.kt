package com.ruben.apiremota.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonEntity(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo(name = "base_experience") val base_experience: Int,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "order") val order: Int,
    @ColumnInfo(name = "weight") val weight: Int
)