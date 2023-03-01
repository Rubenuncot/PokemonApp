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
import androidx.compose.material.icons.filled.Check
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
import com.ruben.apiremota.MainActivity.Companion.pokemon
import com.ruben.apiremota.MainActivity.Companion.rolls
import com.ruben.apiremota.R
import com.ruben.apiremota.data.PokemonRepository
import com.ruben.apiremota.data.local.PokemonEntity
import com.ruben.apiremota.data.remote.Pokemon
import com.ruben.apiremota.navigation.AppScreens
import com.ruben.apiremota.presentation.PokemonViewModel
import com.ruben.apiremota.presentation.RollsViewModel
import com.ruben.apiremota.ui.components.PokemonCell
import java.util.*

var added = false
var pokemonList = mutableListOf<PokemonEntity>()
var pokemonExists = false

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchScreen(viewModel: PokemonViewModel, navController: NavController, index: Int, repository: PokemonRepository) {
    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                viewModel = viewModel,
                items = listOf("Game", "Search", "List"),
            )
        },
    ) {
        val screenState by viewModel.uiState.collectAsStateWithLifecycle()
        val randomScreenState by viewModel.randomUiState.collectAsStateWithLifecycle()
        val scrollState = rememberScrollState()
        val rollsViewModel = RollsViewModel(repository = repository)
        switchRandomScreen(randomScreenState)
        rollsViewModel.getRolls()
        val rollsScreenState by rollsViewModel.uiState.collectAsStateWithLifecycle()
        when(rollsScreenState){
            is RollsScreenState.Success -> {
                rolls = (rollsScreenState as RollsScreenState.Success).rolls
            }
            else -> {}
        }
        when (index) {
            0 ->
                GuessScreen(pokemon = pokemon, viewModel = viewModel)
            1 ->
                Body(pokemon = pokemon, viewModel, navController)
            2 ->
                BodyMain(
                    screenState = screenState,
                    navController = navController,
                    scrollState = scrollState,
                    viewModel = viewModel
                )
        }
    }
}

@Composable
private fun switchRandomScreen(
    randomScreenState: PokemonRandomScreenState,
) {
    when (randomScreenState) {
        is PokemonRandomScreenState.Success -> {
            pokemon = (randomScreenState).pokemon
            val pokemonRandomEntity = PokemonEntity(
                id = pokemon!!.id,
                pokemon!!.base_experience,
                pokemon!!.height,
                pokemon!!.name,
                pokemon!!.order,
                pokemon!!.weight,
                pokemon!!.flavorTextEntry
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
            pokemon = null
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Body(pokemon: Pokemon?, viewModel: PokemonViewModel, navController: NavController) {
    var pokemonEncontrado by remember {
        mutableStateOf(false)
    }
    val openDialog = remember { mutableStateOf(false) }

    MainActivity.pokemon = null

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
                            text = "$rolls rolls left today",
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
                                                rolls += 1
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
                            when(rolls){
                                0 -> openDialog.value = true
                                else -> {
                                    viewModel.getRandomPokemon()
                                    pokemonEncontrado = true
                                    rolls -= 1
                                }
                            }

                            viewModel.insertRolls(rolls, openDialog.value)
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


@Composable
fun BottomBar(navController: NavController, viewModel: PokemonViewModel, items: List<String>) {
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    when (index) {
                        0 ->
                            Icon(Icons.Default.Search, contentDescription = item)
                        1 ->
                            Icon(
                                painter = painterResource(id = R.drawable.pokeballnegra),
                                contentDescription = item,
                                modifier = Modifier.size(20.dp)
                            )
                        2 ->
                            Icon(Icons.Default.List, contentDescription = item)

                    }
                },
                label = { Text(item) },
                selected = MainActivity.index == index,
                onClick = {
                    pokemon = null
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
                viewModel.getPokemons()
                LoadingAnimation()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GuessScreen(pokemon: Pokemon?, viewModel: PokemonViewModel) {
    var bottomBarClick by remember {
        mutableStateOf(false)
    }
    var fullRolls by remember {
        mutableStateOf(false)
    }
    val pokeName = remember { mutableStateOf(TextFieldValue()) }
    val offset = Offset(3.0f, 8.0f)
    MainActivity.pokemon = null
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
                    if (pokemon != null) {
                        AsyncImage(
                            model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + pokemon!!.id + ".png",
                            modifier = Modifier.width(350.dp),
                            contentDescription = "Hola",
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(bottom = 60.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(30.dp)
                    ) {
                        androidx.compose.material.TextField(
                            value = pokeName.value,
                            onValueChange = { pokeName.value = it },
                            label = { androidx.compose.material.Text("Enter the name of the pokemon") },
                            placeholder = { androidx.compose.material.Text("Pokemon Name") }
                        )
                        Box() {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(15.dp)
                            ) {
                                Button(
                                    onClick = {
                                        if(rolls >= 5){
                                            fullRolls = true
                                        } else {
                                            fullRolls = false
                                            viewModel.getRandomPokemon()
                                        }
                                    },
                                    shape = RoundedCornerShape(100),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = Color.Black,
                                        containerColor = Color(0xFFECE3F6)
                                    ),
                                    modifier = Modifier.shadow(
                                        elevation = 10.dp,
                                        shape = RoundedCornerShape(40)
                                    ),
                                )
                                {
                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(verticalArrangement = Arrangement.Center) {
                                            Icon(Icons.Default.Search, contentDescription = "")
                                        }
                                    }
                                }
                                Button(
                                    onClick = {
                                        if(rolls >= 5){
                                            fullRolls = true
                                        } else {
                                            fullRolls = false
                                            if (pokemon != null) {
                                                if (pokeName.value.text.equals(pokemon.name, ignoreCase = true)) {
                                                    rolls += 1
                                                    viewModel.insertRolls(rolls, openDialog = false)
                                                    bottomBarClick = true
                                                }
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
                                        Icon(Icons.Default.Check, contentDescription = "")
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        if (bottomBarClick) {
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
                    bottomBarClick = false
                },
                title = {
                    Text(text = "Rolls")
                },
                text = {
                    Text(text = "You earn 1 more roll!!")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            bottomBarClick = false
                        }
                    ) {
                        Text("Confirm")
                    }
                }
            )
        }
        if (fullRolls) {
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
                    fullRolls = false
                },
                title = {
                    Text(text = "Rolls")
                },
                text = {
                    Text(text = "You already have 5 rolls.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            fullRolls = false
                        }
                    ) {
                        Text("Confirm")
                    }
                }
            )
        }
    }
}