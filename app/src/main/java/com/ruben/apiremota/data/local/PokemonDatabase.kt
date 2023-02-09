package com.ruben.apiremota.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ruben.apiremota.data.local.Result

@Database(entities = [Pokemon::class], version = 1)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao

}
