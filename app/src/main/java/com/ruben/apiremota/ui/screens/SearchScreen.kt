package com.ruben.apiremota.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.Card
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ruben.apiremota.MainActivity
import com.ruben.apiremota.R
import com.ruben.apiremota.data.local.PokemonEntity
import com.ruben.apiremota.data.remote.Pokemon
import com.ruben.apiremota.navigation.AppScreens
import com.ruben.apiremota.presentation.PokemonViewModel
import com.ruben.apiremota.ui.components.ErrorBlock
import com.ruben.apiremota.ui.components.PokemonCell
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

var added = false
var bottomBarClickClass = false
var pokemonList = mutableListOf<PokemonEntity>()
var pokemonExists = false

@RequiresApi(Build.VERSION_CODES.O)
val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchScreen(viewModel: PokemonViewModel, navController: NavController, index: Int) {
    Scaffold(
        bottomBar = { BottomBar(navController, viewModel) },
    ) {
        val screenState by viewModel.uiState.collectAsStateWithLifecycle()
        val randomScreenState by viewModel.randomUiState.collectAsStateWithLifecycle()
        val scrollState = rememberScrollState()

        if (index == 2) {
            BodyMain(
                screenState = screenState,
                navController = navController,
                scrollState = scrollState,
                viewModel = viewModel
            )
        } else {
            when (randomScreenState) {
                is PokemonRandomScreenState.Success -> {
                    MainActivity.pokemonRandom = (randomScreenState as PokemonRandomScreenState.Success).pokemon
                    val pokemonRandomEntity = PokemonEntity(
                        id = MainActivity.pokemonRandom!!.id,
                        MainActivity.pokemonRandom!!.base_experience,
                        MainActivity.pokemonRandom!!.height,
                        MainActivity.pokemonRandom!!.name,
                        MainActivity.pokemonRandom!!.order,
                        MainActivity.pokemonRandom!!.weight,
                        MainActivity.pokemonRandom!!.flavorTextEntry
                    )
                    if (pokemonList.contains(pokemonRandomEntity)) {
                        pokemonExists = true
                    } else {
                        if (index == 1){
                            pokemonList.add(pokemonRandomEntity)
                        }
                    }
                }
                is PokemonRandomScreenState.Error ->
                    ErrorBlock(message = "Ha ocurrido un error") { viewModel.getRandomPokemon() }
                PokemonRandomScreenState.Loading -> Column() {
                    viewModel.getRandomPokemon()
                }

            }
            if (index == 0) {
                viewModel.getRandomPokemon()
                if (MainActivity.pokemonRandom != null){
                    GuessScreen(viewModel = viewModel)
                    bottomBarClickClass = true
                }
            } else {
                Body(pokemon = MainActivity.pokemonRandom, viewModel)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Body(pokemon: Pokemon?, viewModel: PokemonViewModel) {
    var pokemonEncontrado by remember {
        mutableStateOf(false)
    }
    val openDialog = remember { mutableStateOf(false) }

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
            ) {
                Card(
                    modifier = Modifier
                        .width(400.dp)
                        .height(450.dp)
                        .padding(20.dp)
                        .shadow(elevation = 10.dp),
                    backgroundColor = Color(0xFFECE3F6)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "" + MainActivity.rolls + " rolls",
                            modifier = Modifier
                                .background(Color(0xFFFFFBD1), shape = RoundedCornerShape(100))
                                .padding(3.dp)
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Row {
                                    Column(
                                        verticalArrangement = Arrangement.Center
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
                                    }
                                    Column {
                                        if (pokemonEncontrado) {
                                            if (pokemon != null) {
                                                Text(
                                                    text = "Name: " + pokemon.name,
                                                    fontWeight = FontWeight.Bold,
                                                    style = TextStyle(textAlign = TextAlign.Center)
                                                )
                                                Text(text = "Pokedex Number: " + pokemon.id)
                                            }
                                            if (pokemonExists) {
                                                Text(
                                                    text = "Ohh, you already found this pokemon. But don't worry, now you earn 1 more roll to found a new pokemon.",
                                                    style = TextStyle(textAlign = TextAlign.Center)
                                                )
                                                pokemonExists = false
                                                MainActivity.rolls += 1
                                            } else {
                                                Text(
                                                    text = "Congratulations!!!!, you found a new pokemon, let's see it",
                                                    style = TextStyle(textAlign = TextAlign.Center)
                                                )
                                            }
                                        }
                                        if (openDialog.value) {
                                            //Text(
                                            //text = "You don't have any rolls left, wait until tomorrow so you can roll once again",
                                            //fontWeight = FontWeight.Bold,
                                            //style = TextStyle(textAlign = TextAlign.Center)
                                            //)
                                            AlertDialog(
                                                onDismissRequest = {
                                                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                                                    // button. If you want to disable that functionality, simply use an empty
                                                    // onDismissRequest.
                                                    openDialog.value = false
                                                },
                                                title = {
                                                    Text(text = "Rolls")
                                                },
                                                text = {
                                                    Text(text = "You don't have any rolls left, wait until tomorrow so you can roll once again")
                                                },
                                                confirmButton = {
                                                    TextButton(
                                                        onClick = {
                                                            openDialog.value = false
                                                        }
                                                    ) {
                                                        Text("Confirm")
                                                    }
                                                },
                                                dismissButton = {
                                                    TextButton(
                                                        onClick = {
                                                            openDialog.value = false
                                                        }
                                                    ) {
                                                        Text("Dismiss")
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Box() {
                    Button(
                        onClick = {
                            viewModel.getRandomPokemon()
                            pokemonEncontrado = true
                            MainActivity.rolls -= 1
                            if (MainActivity.rolls == 0) {
                                openDialog.value = true
                            } else if (MainActivity.rolls == 4) {
                                MainActivity.timeNow = LocalDateTime.now()
                            }
                            viewModel.inserRolls(MainActivity.rolls, openDialog.value)
                        },
                        shape = RoundedCornerShape(100),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.Black,
                            containerColor = Color(0xFFECE3F6)
                        ),
                        modifier = Modifier.shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(100)
                        ),
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
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomBar(navController: NavController, viewModel: PokemonViewModel) {
    val items = listOf("Guess", "Search", "List")

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    if (index == 0) {
                        Icon(Icons.Default.Search, contentDescription = item)

                    } else if (index == 1) {
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
                    viewModel.getRandomPokemon()
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
            PokemonScreenState.Loading -> Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoadingAnimation()
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GuessScreen(viewModel: PokemonViewModel) {
    var bottomBarClick by remember {
        mutableStateOf(bottomBarClickClass)
    }
    val pokeName = remember { mutableStateOf(TextFieldValue()) }
    val offset = Offset(3.0f, 8.0f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(Modifier.padding(20.dp)) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(5)),
                shape = RoundedCornerShape(5),
                backgroundColor = Color(0xFFECE3F6)
            ) {
                Column() {
                    androidx.compose.material.Text(
                        text = "Who's that Pokemon?",
                        modifier = Modifier.width(350.dp),
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 35.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            shadow = Shadow(
                                color = Color.White, offset = offset, blurRadius = 2f
                            )
                        )
                    )
                    if(bottomBarClick){
                        if (MainActivity.pokemonRandom != null) {
                            AsyncImage(
                                model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + MainActivity.pokemonRandom!!.id + ".png",
                                modifier = Modifier.width(350.dp),
                                contentDescription = "Hola"
                            )
                        }
                        bottomBarClickClass = false
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(bottom = 60.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        androidx.compose.material.TextField(
                            value = pokeName.value,
                            onValueChange = { pokeName.value = it },
                            label = { androidx.compose.material.Text("Enter the name of the pokemon") },
                            placeholder = { androidx.compose.material.Text("Pokemon Name") }
                        )
                        Box() {
                            Button(
                                onClick = {
                                    viewModel.getRandomPokemon()
                                    if (MainActivity.pokemonRandom != null) {
                                        if (pokeName.equals(MainActivity.pokemonRandom!!.name + "")) {
                                            MainActivity.rolls += 1
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(100),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.Black,
                                    containerColor = Color(0xFFECE3F6)
                                ),
                                modifier = Modifier.shadow(
                                    elevation = 10.dp,
                                    shape = RoundedCornerShape(100)
                                ),
                            )
                            {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    androidx.compose.material.Text(
                                        text = "CHECK",
                                        modifier = Modifier.size(50.dp)
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}

