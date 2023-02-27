package com.ruben.apiremota.data.remote

import com.ruben.apiremota.data.local.FlavorTextEntry
import kotlin.random.Random

class PokemonRemoteDatasource( private val apiService: ApiService) {
    suspend fun getPokemons(offset: Int, limit: Int) = apiService.getPokemons(offset, limit)
    suspend fun getPokemonsByIds(ids: MutableList<Int>): MutableList<Pokemon> {
        val pokemons: MutableList<Pokemon> = mutableListOf()
        ids.forEach{
            pokemons.add(apiService.getPokemon(it))
        }
        return pokemons
    }
    suspend fun getRandomPokemonByIDd(): Pokemon {
        return apiService.getPokemon(randomNum())
    }
    fun randomNum(): Int{
        val list = 1 .. 1008

        return list.random(Random(list.shuffled().random(Random(list.random()))))
    }

    suspend fun getSpecie(id: Int): List<FlavorTextEntry>{
        return apiService.getSpecie(id).flavor_text_entries
    }
}