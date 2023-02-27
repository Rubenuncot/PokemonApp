package com.ruben.apiremota.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ruben.apiremota.MainActivity
import com.ruben.apiremota.R
import com.ruben.apiremota.data.local.PokemonEntity
import com.ruben.apiremota.navigation.AppScreens
import com.ruben.apiremota.presentation.PokemonViewModel
import com.ruben.apiremota.ui.theme.Rolls

var rolls = Rolls
var pokemon: PokemonEntity? = null


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchScreen(viewModel: PokemonViewModel, navController: NavController, index: Int) {
    Scaffold(
        bottomBar = { BottomBar(navController) },
    ) {
        val screenState by viewModel.uiState.collectAsStateWithLifecycle()
        val randomScreenState by viewModel.randomUiState.collectAsStateWithLifecycle()
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
                    Body(pokemon = pokemon, viewModel)
                    when(randomScreenState){
                        is PokemonRandomScreenState.Success -> {
                            pokemon = (randomScreenState as PokemonRandomScreenState.Success).pokemon
                        }
                        is PokemonRandomScreenState.Error ->
                            Text(text = "Ha ocurrido un fallo inesperado")
                        PokemonRandomScreenState.Loading -> Column (
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            LoadingAnimation()
                        }
                    }
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
fun Body(pokemon: PokemonEntity?, viewModel: PokemonViewModel) {
    var pokemonEncontrado by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = Modifier
                .width(400.dp)
                .height(550.dp)
                .padding(20.dp)
                .shadow(elevation = 10.dp)) {
            if (pokemon == null){
                AsyncImage(
                    model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            } else {
                AsyncImage(
                    model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"+ pokemon.id + ".png",
                    contentDescription = null,
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .offset(y = (-70).dp),

            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                            viewModel.getRandomPokemon()
                            pokemonEncontrado = true
                          },
                shape = RoundedCornerShape(100),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = Color(0xFFECE3F6)
                ),
                modifier = Modifier.shadow(elevation = 10.dp, shape = RoundedCornerShape(100)),
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

@Composable
fun LoadingAnimation(
    circleColor: Color = Color.Magenta,
    animationDelay: Int = 1000,
) {

    // circle's scale state
    var circleScale by remember {
        mutableStateOf(0f)
    }

    // animation
    val circleScaleAnimate = animateFloatAsState(
        targetValue = circleScale,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDelay
            )
        )
    )

    // This is called when the app is launched
    LaunchedEffect(Unit) {
        circleScale = 1f
    }

    // animating circle
    Box(
        modifier = Modifier
            .size(size = 64.dp)
            .scale(scale = circleScaleAnimate.value)
            .border(
                width = 4.dp,
                color = circleColor.copy(alpha = 1 - circleScaleAnimate.value),
                shape = CircleShape
            )
    ) {

    }
}