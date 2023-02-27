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

    if (endReached || viewModel.currentPage == 0) {
        LaunchedEffect(viewModel) {
            viewModel.getPokemons()
        }
    }
}

