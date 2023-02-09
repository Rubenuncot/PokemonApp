package com.ruben.apiremota.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ruben.apiremota.presentation.PokemonCell
import com.ruben.apiremota.presentation.PokemonViewModel
import com.ruben.apiremota.ui.screens.PokemonScreenState

@OptIn(ExperimentalLifecycleComposeApi ::class)
@Composable
fun MainScreen (viewModel: PokemonViewModel) {
    val screenState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val endReached by remember {
        derivedStateOf {
            scrollState.value >= scrollState.maxValue
        }
    }


    Box(
        contentAlignment = Alignment. Center, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        when (screenState) {
            PokemonScreenState.Loading -> CircularProgressIndicator(modifier = Modifier. size(48.dp))
            is PokemonScreenState.Error ->
                ErrorBlock(message = (screenState as PokemonScreenState.Error).message) { viewModel.getPokemon() }
            is PokemonScreenState.Success ->
                Column(
                    modifier = Modifier. verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy( 16.dp)
                ) {
                    (screenState as PokemonScreenState.Success).pokemon.forEach {
                    PokemonCell(it)
                    }
                }
        }
    }
    LaunchedEffect(viewModel) {
        viewModel.getPokemon()
    }

    if(endReached){
        LaunchedEffect(viewModel){
            viewModel.getNextPokemon()
        }
    }
}