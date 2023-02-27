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
        val pokemon: Pokemon
        val id = Random.nextInt(1, 1008)
        pokemon = apiService.getPokemon(id)
        return pokemon
    }
}