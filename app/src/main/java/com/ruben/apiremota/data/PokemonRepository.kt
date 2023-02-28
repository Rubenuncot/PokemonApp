package com.ruben.apiremota.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.ruben.apiremota.MainActivity
import com.ruben.apiremota.data.local.FlavorTextEntry
import com.ruben.apiremota.data.local.PokemonDatasource
import com.ruben.apiremota.data.local.PokemonEntity
import com.ruben.apiremota.data.local.RollsDatasource
import com.ruben.apiremota.data.remote.Pokemon
import com.ruben.apiremota.data.remote.PokemonRemoteDatasource
import com.ruben.apiremota.navigation.AppScreens
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class PokemonRepository(
    private val pokemonLocalDatasource: PokemonDatasource,
    private val pokemonRemoteDatasource: PokemonRemoteDatasource,
    private val rollsDatasource: RollsDatasource
) {
    suspend fun getPokemons(): List<PokemonEntity> {
        /*val apiResponse = pokemonRemoteDatasource.getPokemons(offset, Limit)
        var pokemonUrl: String
        val pokemonIds: MutableList<Int> = mutableListOf()

        apiResponse.results.forEach {
            pokemonUrl = it.url
            val urlSplitted = pokemonUrl.split("/")
            val id = urlSplitted[urlSplitted.size-2]
            pokemonIds.add(id.toInt())
        }
        val pokemons = pokemonRemoteDatasource.getPokemonsByIds(pokemonIds)
        pokemonLocalDatasource.createAllPokemons(pokemons.toLocalEntity())*/
        return pokemonLocalDatasource.getPokemons()
    }

    suspend fun getRandomPokemon(): Pokemon {
        val apiResponse = pokemonRemoteDatasource.getRandomPokemonByIDd()
        apiResponse.flavorTextEntry = getSpecie(apiResponse.id).flavor_text
        pokemonLocalDatasource.createPokemon(apiResponse)
        return apiResponse
    }

    suspend fun getSpecie(id: Int): FlavorTextEntry {
        val apiResponse = pokemonRemoteDatasource.getSpecie(id)
        return apiResponse[1]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getRolls(): Int {
        val begin = LocalDateTime.now()
        if (begin.compareTo(MainActivity.timeNow) == 0) {
            return rollsDatasource.getLast().rolls
        }
        return 0
    }

    suspend fun insertRolls(rolls: Int, openDialog: Boolean){
        rollsDatasource.insertRolls(rolls, openDialog)
    }
}