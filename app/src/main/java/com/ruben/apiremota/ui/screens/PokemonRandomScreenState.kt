package com.ruben.apiremota.ui.screens

import com.ruben.apiremota.data.remote.Pokemon

sealed class PokemonRandomScreenState {
    object Loading: PokemonRandomScreenState()
    data class Error(val message: String): PokemonRandomScreenState()
    data class Success(val pokemon: Pokemon): PokemonRandomScreenState()
}