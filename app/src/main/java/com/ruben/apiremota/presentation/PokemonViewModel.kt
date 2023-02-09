package com.ruben.apiremota.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruben.apiremota.data.local.PokemonRemoteDatasource
import com.ruben.apiremota.ui.screens.PokemonScreenState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonViewModel( private val pokemonRemoteDatasource : PokemonRemoteDatasource) :
    ViewModel() {
    private val _uiState: MutableStateFlow<PokemonScreenState> = MutableStateFlow(PokemonScreenState.Loading)
    val uiState: StateFlow<PokemonScreenState> = _uiState.asStateFlow()
    val pokemonIds: MutableList<Int> = mutableListOf()

    private var currentPage: Int = 0
    private val limit: Int = 6


    private val handler = CoroutineExceptionHandler { _, _ ->
        _uiState.value = PokemonScreenState.Error( "Ha ocurrido un error, revise su conexión a internet o inténtelo de nuevo " +
            "más tarde" )
    }
    fun getPokemon () {
        viewModelScope.launch(handler) {
            //_uiState.value = PokemonScreenState.Loading
            val page = pokemonRemoteDatasource.getPokemons(currentPage, limit)
            var pokemonUrl: String

            page.results.forEach {
                pokemonUrl = it.url
                val urlSplitted = pokemonUrl.split("/")
                val id = urlSplitted[urlSplitted.size-2]
                pokemonIds.add(id.toInt())
            }
            val pokemons = pokemonRemoteDatasource.getPokemonsByIds(pokemonIds)
            _uiState.value = PokemonScreenState.Success(pokemons)
        }
    }

    fun getNextPokemon () {
        currentPage++
        viewModelScope.launch(handler) {
            //_uiState.value = PokemonScreenState.Loading
            val page = pokemonRemoteDatasource.getPokemons(currentPage * limit, limit)
            var pokemonUrl: String

            page.results.forEach {
                pokemonUrl = it.url
                val urlSplitted = pokemonUrl.split("/")
                val id = urlSplitted[urlSplitted.size-2]
                pokemonIds.add(id.toInt())
            }
            val pokemons = pokemonRemoteDatasource.getPokemonsByIds(pokemonIds)
            _uiState.value = PokemonScreenState.Success(pokemons)
        }
    }
}