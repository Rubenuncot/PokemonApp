package com.ruben.apiremota.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruben.apiremota.data.PokemonRepository
import com.ruben.apiremota.ui.screens.PokemonScreenState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonViewModel( private val repository: PokemonRepository) :
    ViewModel() {
    private val _uiState: MutableStateFlow<PokemonScreenState> = MutableStateFlow(PokemonScreenState.Loading)
    val uiState: StateFlow<PokemonScreenState> = _uiState.asStateFlow()

    private var currentPage: Int = 0


    private val handler = CoroutineExceptionHandler { _, _ ->
        _uiState.value = PokemonScreenState.Error( "Ha ocurrido un error, revise su conexión a internet o inténtelo de nuevo " +
            "más tarde" )
    }
    fun getPokemon () {
        viewModelScope.launch(handler) {
            val pokemons = repository.getPokemon(currentPage)
            _uiState.value = PokemonScreenState.Success(pokemons)
        }
    }

    fun getNextPokemon () {
        currentPage++
        viewModelScope.launch(handler) {
            val pokemons = repository.getPokemon(currentPage)
            _uiState.value = PokemonScreenState.Success(pokemons)
        }
    }
}