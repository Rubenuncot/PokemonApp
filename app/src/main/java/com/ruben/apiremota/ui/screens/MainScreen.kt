package com.ruben.apiremota.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ruben.apiremota.presentation.PokemonViewModel
import com.ruben.apiremota.ui.components.ErrorBlock
import com.ruben.apiremota.ui.components.PokemonCell

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun MainScreen (viewModel: PokemonViewModel, navController: NavController) {
    val screenState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val endReached by remember {
        derivedStateOf {
            scrollState.value >= scrollState.maxValue
        }
    }
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        BodyMain(
            screenState = screenState,
            navController = navController,
            scrollState = scrollState,
            viewModel = viewModel
        )
    }

    if (endReached || viewModel.currentPage == 0) {
        LaunchedEffect(viewModel) {
            viewModel.getPokemons()
        }
    }
}

@Composable
fun BodyMain(
    screenState: PokemonScreenState,
    navController: NavController,
    scrollState: ScrollState,
    viewModel: PokemonViewModel
) {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        when (screenState) {
            PokemonScreenState.Loading -> Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                LoadingAnimation()
            }
            is PokemonScreenState.Error ->
                ErrorBlock(message = (screenState).message) { viewModel.getPokemons() }
            is PokemonScreenState.Success ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    (screenState as PokemonScreenState.Success).pokemon.forEach {
                        PokemonCell(it, navController)
                    }
                }
        }
    }
}