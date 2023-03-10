package com.ruben.apiremota.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ruben.apiremota.ui.theme.Rolls

@Database(entities = [PokemonEntity::class, RollsEntity::class], version = 1)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun rollsDao(): RollsDao
}
