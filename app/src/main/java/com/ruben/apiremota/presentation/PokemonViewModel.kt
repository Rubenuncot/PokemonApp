package com.ruben.apiremota.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruben.apiremota.data.PokemonRepository
import com.ruben.apiremota.ui.screens.PokemonRandomScreenState
import com.ruben.apiremota.ui.screens.PokemonScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<PokemonScreenState> = MutableStateFlow(PokemonScreenState.Loading)
    private val _uiStateRandom : MutableStateFlow<PokemonRandomScreenState> = MutableStateFlow(PokemonRandomScreenState.Loading)
    val uiState: StateFlow<PokemonScreenState> = _uiState.asStateFlow()
    val randomUiState: StateFlow<PokemonRandomScreenState> = _uiStateRandom.asStateFlow()


    fun getPokemons() {
        viewModelScope.launch {
            val pokemons = repository.getPokemons()
            val currentPokemons = if (_uiState.value is PokemonScreenState.Success) {
                (_uiState.value as PokemonScreenState.Success).pokemon
            } else emptyList()
            val totalPokemons = currentPokemons + pokemons
            _uiState.value = PokemonScreenState.Success(totalPokemons)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRandomPokemon(){
        viewModelScope.launch{
            val pokemon = repository.getRandomPokemon()
            _uiStateRandom.value = PokemonRandomScreenState.Success(pokemon = pokemon)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRolls(): Int{
        var rolls = 0
        viewModelScope.launch {
             rolls = repository.getRolls()
        }
        return rolls
    }

    fun insertRolls(rolls: Int, openDialog: Boolean){
        viewModelScope.launch {
            repository.insertRolls(rolls, openDialog)
        }
    }
}