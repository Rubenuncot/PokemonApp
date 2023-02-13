package com.ruben.apiremota.data

import com.ruben.apiremota.data.local.PokemonDatasource
import com.ruben.apiremota.data.local.PokemonEntity
import com.ruben.apiremota.data.remote.PokemonRemoteDatasource
import com.ruben.apiremota.data.remote.toLocalEntity

class PokemonRepository (
    private val pokemonLocalDatasource: PokemonDatasource,
    private val pokemonRemoteDatasource: PokemonRemoteDatasource
){
    private var maxPages = 1
    private val pokemonIds: MutableList<Int> = mutableListOf()

    suspend fun syncronyze(currentPage: Int){
        val offset = currentPage
        val page = pokemonRemoteDatasource.getPokemons(offset, Limit)
        var pokemonUrl: String

        page.results.forEach {
            pokemonUrl = it.url
            val urlSplitted = pokemonUrl.split("/")
            val id = urlSplitted[urlSplitted.size-2]
            pokemonIds.add(id.toInt())
        }
        val pokemons = pokemonRemoteDatasource.getPokemonsByIds(pokemonIds)
        pokemonLocalDatasource.createAllPokemon(pokemons.toLocalEntity())
    }

    suspend fun getPokemon(currentPage: Int): List<PokemonEntity> {
        val offset = currentPage
        syncronyze(offset)
        return pokemonLocalDatasource.getPokemon(offset)
    }

}