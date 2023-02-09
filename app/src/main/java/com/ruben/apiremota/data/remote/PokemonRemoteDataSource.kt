package com.ruben.apiremota.data.local

import com.ruben.apiremota.data.remote.ApiService

class PokemonRemoteDatasource( private val apiService: ApiService) {
    suspend fun getPokemons(offset: Int, limit: Int) = apiService.getPokemons(offset, limit)
    suspend fun getPokemonsByIds(ids: MutableList<Int>): MutableList<Pokemon> {
        val pokemons: MutableList<Pokemon> = mutableListOf()
        ids.forEach{
            pokemons.add(apiService.getPokemon(it))
        }
        return pokemons
    }
}