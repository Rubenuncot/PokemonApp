package com.ruben.apiremota.data

import com.ruben.apiremota.data.local.PokemonDatasource
import com.ruben.apiremota.data.local.PokemonEntity
import com.ruben.apiremota.data.remote.PokemonRemoteDatasource
import com.ruben.apiremota.data.remote.toLocalEntity

class PokemonRepository (
    private val pokemonLocalDatasource: PokemonDatasource,
    private val pokemonRemoteDatasource: PokemonRemoteDatasource
){
    suspend fun getPokemons(currentPage: Int): List<PokemonEntity> {
        //TODO: Check max pages
        val offset = currentPage * Limit
        val apiResponse = pokemonRemoteDatasource.getPokemons(offset, Limit)
        var pokemonUrl: String
        val pokemonIds: MutableList<Int> = mutableListOf()

        apiResponse.results.forEach {
            pokemonUrl = it.url
            val urlSplitted = pokemonUrl.split("/")
            val id = urlSplitted[urlSplitted.size-2]
            pokemonIds.add(id.toInt())
        }
        val pokemons = pokemonRemoteDatasource.getPokemonsByIds(pokemonIds)
        pokemonLocalDatasource.createAllPokemons(pokemons.toLocalEntity())
        return pokemonLocalDatasource.getPokemons(offset)
    }

}