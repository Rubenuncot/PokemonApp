package com.ruben.apiremota.data.remote

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
        val list2 = 1 .. 1008

        return list.random(Random(list2.shuffled().random(Random(list.random()))))
    }
}