package com.ruben.apiremota.ui.screens

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ruben.apiremota.MainActivity
import com.ruben.apiremota.R
import com.ruben.apiremota.navigation.AppScreens
import com.ruben.apiremota.presentation.PokemonViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchScreen(viewModel: PokemonViewModel, navController: NavController, index: Int) {
    Scaffold(
        bottomBar = { BottomBar(navController) },
    ) {
        val screenState by viewModel.uiState.collectAsStateWithLifecycle()
        val scrollState = rememberScrollState()

        val endReached by remember {
            derivedStateOf {
                scrollState.value >= scrollState.maxValue
            }
        }
        Scaffold(
            bottomBar = { BottomBar(navController = navController) },
            topBar = {
                if (index == 1) {
                    BodyMain(
                        screenState = screenState,
                        navController = navController,
                        scrollState = scrollState,
                        viewModel = viewModel
                    )
                } else {
                    Body()
                }
            }
        ) {

        }

        if (endReached || viewModel.currentPage == 0) {
            LaunchedEffect(viewModel) {
                viewModel.getPokemons()
            }
        }

    }
}

@Composable
fun Body() {
    val interactionSource = remember { MutableInteractionSource() }
    val interactions = remember { mutableStateListOf<Interaction>() }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card {
            AsyncImage(
                model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                contentDescription = null,
            )
        }

        Button(
            onClick = { },
            shape = RoundedCornerShape(100),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = Color(0xFFECE3F6)
            ),
            modifier = Modifier.shadow(elevation = 10.dp, shape = RoundedCornerShape(100)),
            interactionSource = interactionSource
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.pokeballnegra),
                    contentDescription = "pokeball",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf("Search", "List")

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    if (index == 0) {
                        Icon(
                            painter = painterResource(id = R.drawable.pokeballnegra),
                            contentDescription = item,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(Icons.Default.List, contentDescription = item)
                    }
                },
                label = { Text(item) },
                selected = MainActivity.index == index,
                onClick = {
                    navController.navigate(AppScreens.Search.route)
                    MainActivity.index = index
                }
            )
        }
    }
}

