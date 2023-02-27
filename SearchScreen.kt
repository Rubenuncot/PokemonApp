package com.ruben.apiremota.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.size.Size
import com.ruben.apiremota.MainActivity
import com.ruben.apiremota.R
import com.ruben.apiremota.data.local.PokemonEntity
import com.ruben.apiremota.data.remote.Pokemon
import com.ruben.apiremota.navigation.AppScreens
import com.ruben.apiremota.presentation.PokemonViewModel
import com.ruben.apiremota.ui.components.ErrorBlock
import com.ruben.apiremota.ui.components.PokemonCell
import com.ruben.apiremota.ui.theme.Rolls

var rolls = Rolls
var added = false
var pokemonRandom: Pokemon? = null
var pokemonList = mutableListOf<PokemonEntity>()
var pokemonExists = false

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchScreen(viewModel: PokemonViewModel, navController: NavController, index: Int) {
    Scaffold(
        bottomBar = { BottomBar(navController, viewModel) },
    ) {
        val screenState by viewModel.uiState.collectAsStateWithLifecycle()
        val randomScreenState by viewModel.randomUiState.collectAsStateWithLifecycle()
        val scrollState = rememberScrollState()

        val endReached by remember {
            derivedStateOf {
                scrollState.value >= scrollState.maxValue
            }
        }
        if (index == 1) {
            BodyMain(
                screenState = screenState,
                navController = navController,
                scrollState = scrollState,
                viewModel = viewModel
            )
        } else {
            when (randomScreenState) {
                is PokemonRandomScreenState.Success -> {
                    pokemonRandom = (randomScreenState as PokemonRandomScreenState.Success).pokemon
                    val pokemonRandomEntity = PokemonEntity(
                        id = pokemonRandom!!.id,
                        pokemonRandom!!.base_experience,
                        pokemonRandom!!.height,
                        pokemonRandom!!.name,
                        pokemonRandom!!.order,
                        pokemonRandom!!.weight
                    )
                    if (pokemonList.contains(pokemonRandomEntity)) {
                        pokemonExists = true
                    } else {
                        pokemonList.add(pokemonRandomEntity)
                    }
                }
                is PokemonRandomScreenState.Error ->
                    Text(text = "Ha ocurrido un fallo inesperado")
                PokemonRandomScreenState.Loading -> Column() {

                }
            }
            Body(pokemon = pokemonRandom, viewModel)
        }
    }
}

@Composable
fun Body(pokemon: Pokemon?, viewModel: PokemonViewModel) {
    var pokemonEncontrado by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .offset(y = (-70).dp),

            contentAlignment = Alignment.Center,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Card(
                    modifier = Modifier
                        .width(400.dp)
                        .height(450.dp)
                        .padding(20.dp)
                        .shadow(elevation = 10.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (pokemon == null) {
                                AsyncImage(
                                    model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(Color.Black)
                                )
                            } else {
                                AsyncImage(
                                    model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + pokemon.id + ".png",
                                    contentDescription = null,
                                )
                            }
                            if (pokemonEncontrado) {
                                if (pokemon != null) {
                                    Text(text = "Name: " + pokemon.name, fontWeight = FontWeight.Bold, style = TextStyle(textAlign = TextAlign.Center))
                                    Text(text = "Pokédex Number: " + pokemon.id)
                                    rolls -= 1

                                }
                                if (pokemonExists) {
                                    Text(text = "Ohh, you already found this pokemon. But don't worry, now you earn 1 more roll to found a new pokemon.", style = TextStyle(textAlign = TextAlign.Center))
                                    pokemonExists = false
                                    rolls += 1
                                } else {
                                    if (pokemon != null) {
                                        Text(text = "!!Congratulations, you found: " + pokemon.name, style = TextStyle(textAlign = TextAlign.Center))
                                    }
                                }
                            }
                            Text(text = "Number of rolls: " + rolls, fontWeight = FontWeight.Bold, style = TextStyle(textAlign = TextAlign.Center))
                            if (rolls==0){
                                Text(text = "You don't have any rolls left, wait until tomorrow so you can roll once again", fontWeight = FontWeight.Bold, style = TextStyle(textAlign = TextAlign.Center))
                            }
                        }
                    }

                }
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
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavController, viewModel: PokemonViewModel) {
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
                    viewModel.getPokemons()
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
            .size(563.dp)
    ) {
        when (screenState) {
            PokemonScreenState.Loading -> Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                    if (!added) {
                        pokemonList = mutableListOf()
                        screenState.pokemon.forEach {
                            pokemonList.add(it)
                            PokemonCell(pokemon = it, navController = navController)
                        }
                        added = true
                    } else {
                        pokemonList.forEach {
                            PokemonCell(pokemon = it, navController = navController)
                        }
                    }
                }
        }
    }
}