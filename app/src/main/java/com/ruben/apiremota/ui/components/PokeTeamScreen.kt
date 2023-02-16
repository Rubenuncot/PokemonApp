package com.ruben.apiremota.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ruben.apiremota.data.remote.Pokemon

val pokemonList = listOf(
    Pokemon(100, 30,1,"Pikachu",1,50),
)

@Composable
    fun PokemonCard(pokemon: Pokemon) {
        Card(
            backgroundColor = Color.White,
            elevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = pokemon.name, style = MaterialTheme.typography.h5)
            }
        }
    }

    @Composable
    fun PokemonList(pokemonList: List<Pokemon>) {
        Column {
            for (pokemon in pokemonList) {
                PokemonCard(pokemon = pokemon)
            }
        }
    }

@Composable
fun SixPokemonScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Tu Equipo Pokémon") }) }
    ) {
        PokemonList(pokemonList = pokemonList)
    }
}
