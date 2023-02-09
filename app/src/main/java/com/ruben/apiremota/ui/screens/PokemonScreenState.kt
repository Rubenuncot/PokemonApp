package com.ruben.apiremota.ui.screens

import com.ruben.apiremota.data.local.Pokemon

sealed class PokemonScreenState {
    object Loading: PokemonScreenState()
    data class Error(val message: String): PokemonScreenState()
    data class Success(val pokemon: List<Pokemon>): PokemonScreenState()
}