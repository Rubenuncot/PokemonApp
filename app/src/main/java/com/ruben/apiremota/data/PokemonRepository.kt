package com.ruben.apiremota.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.ruben.apiremota.data.local.*
import com.ruben.apiremota.data.remote.Pokemon
import com.ruben.apiremota.data.remote.PokemonRemoteDatasource

class PokemonRepository(
    private val pokemonLocalDatasource: PokemonDatasource,
    private val pokemonRemoteDatasource: PokemonRemoteDatasource,
    private val rollsDatasource: RollsDatasource
) {
    suspend fun getPokemons(): List<PokemonEntity> {
        return pokemonLocalDatasource.getPokemons()
    }

    suspend fun getRandomPokemon(insert: Boolean): Pokemon {
        val apiResponse = pokemonRemoteDatasource.getRandomPokemonByIDd()
        apiResponse.flavorTextEntry = getSpecie(apiResponse.id).flavor_text
        if (insert){
            pokemonLocalDatasource.createPokemon(apiResponse)
        }
        return apiResponse
    }

    private suspend fun getSpecie(id: Int): FlavorTextEntry {
        val apiResponse = pokemonRemoteDatasource.getSpecie(id)
        return if (apiResponse.isNotEmpty()) {
            apiResponse.first()
        } else {
            FlavorTextEntry("No description available")
        }
    }

    suspend fun getRolls(): Int {
        var rolls = 0
        if(rollsDatasource.getLast() != null){
            rolls = rollsDatasource.getLast().rolls
        }
        return rolls

    }

    suspend fun insertRolls(rolls: Int, openDialog: Boolean) {
        rollsDatasource.insertRolls(rolls, openDialog)
    }

}