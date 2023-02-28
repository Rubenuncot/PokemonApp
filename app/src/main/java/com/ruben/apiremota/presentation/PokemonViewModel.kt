package com.ruben.apiremota.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruben.apiremota.MainActivity
import com.ruben.apiremota.data.PokemonRepository
import com.ruben.apiremota.navigation.AppScreens
import com.ruben.apiremota.ui.screens.PokemonRandomScreenState
import com.ruben.apiremota.ui.screens.PokemonScreenState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<PokemonScreenState> = MutableStateFlow(PokemonScreenState.Loading)
    private val _uiStateRandom : MutableStateFlow<PokemonRandomScreenState> = MutableStateFlow(PokemonRandomScreenState.Loading)
    val uiState: StateFlow<PokemonScreenState> = _uiState.asStateFlow()
    val randomUiState: StateFlow<PokemonRandomScreenState> = _uiStateRandom.asStateFlow()

    private val handler = CoroutineExceptionHandler { _, _ ->
        _uiState.value = PokemonScreenState.Error(
            "Ha ocurrido un error, revise su conexión a internet o inténtelo de nuevo " +
                "más tarde"
        )
    }

    fun getPokemons() {
        viewModelScope.launch(handler) {
            val pokemons = repository.getPokemons()
            val currentPokemons = if (_uiState.value is PokemonScreenState.Success) {
                (_uiState.value as PokemonScreenState.Success).pokemon
            } else if (_uiState.value is PokemonScreenState.Loading){
                (_uiState.value as PokemonScreenState.Success).pokemon
            } else emptyList()
            val totalPokemons = currentPokemons + pokemons
            _uiState.value = PokemonScreenState.Success(totalPokemons)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRandomPokemon(){
        viewModelScope.launch(handler) {
            if (MainActivity.rolls > 0){
                val pokemon = repository.getRandomPokemon()
                _uiStateRandom.value = PokemonRandomScreenState.Success(pokemon = pokemon)
            } else {
                getRolls()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRolls(){
        viewModelScope.launch(handler) {
            MainActivity.rolls = repository.getRolls()
        }
    }

    fun inserRolls(rolls: Int, openDialog: Boolean){
        viewModelScope.launch {
            repository.insertRolls(rolls, openDialog)
        }
    }
}