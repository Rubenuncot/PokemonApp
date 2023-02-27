package com.ruben.apiremota.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.ruben.apiremota.data.Limit

@Dao
interface PokemonDao {
    @Query("SELECT * FROM PokemonEntity ORDER BY id LIMIT $Limit OFFSET :offset")
    suspend fun getAll(offset: Int): List<PokemonEntity>

    @Query("SELECT * FROM PokemonEntity WHERE id=:id")
    suspend fun getPokemon(id: Int): PokemonEntity

    @Insert(onConflict = REPLACE)
    suspend fun insert(pokemon: PokemonEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(pokemons: List<PokemonEntity>)

    @Delete
    suspend fun delete(pokemon: PokemonEntity)
}