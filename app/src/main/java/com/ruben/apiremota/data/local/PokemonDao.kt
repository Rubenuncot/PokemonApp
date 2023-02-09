package com.ruben.apiremota.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.ruben.apiremota.data.Limit
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemonentity ORDER BY id LIMIT $Limit OFFSET :offset")
    fun getAll(): Flow<List<PokemonEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(pokemon: PokemonEntity)

    @Delete
    suspend fun delete(pokemon: PokemonEntity)
}