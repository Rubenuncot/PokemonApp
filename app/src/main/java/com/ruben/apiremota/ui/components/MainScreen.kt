package com.ruben.apiremota.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ruben.apiremota.presentation.PokemonViewModel
import com.ruben.apiremota.ui.screens.PokemonScreenState

@OptIn(ExperimentalLifecycleComposeApi ::class)
@Composable
fun MainScreen (viewModel: PokemonViewModel, navController: NavController) {
    val screenState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val navItems = listOf(
        NavItem.Home,
        NavItem.Search,
        NavItem.Profile
    )
    var selectedNavItem by remember { mutableStateOf(navItems[0]) }
    val endReached by remember {
        derivedStateOf {
            scrollState.value >= scrollState.maxValue
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigation (modifier = Modifier.zIndex(0f)) {
                navItems.forEach { navItem ->
                    BottomNavigationItem(
                        icon = { Icon(navItem.icon, contentDescription = null) },
                        label = { Text(navItem.title) },
                        selected = selectedNavItem == navItem,
                        onClick = { selectedNavItem = navItem }
                    )
                }
            }
        }
    ) { innerPadding ->
        // The content of your screen goes here
        // Use the "innerPadding" parameter to avoid content being
        // covered by the bottom navigation bar
    }

    Box(
        contentAlignment = Alignment. Center, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        when (screenState) {
            PokemonScreenState.Loading -> CircularProgressIndicator(modifier = Modifier. size(48.dp))
            is PokemonScreenState.Error ->
                ErrorBlock(message = (screenState as PokemonScreenState.Error).message) { viewModel.getPokemons() }
            is PokemonScreenState.Success ->
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy( 16.dp)
                ) {
                    (screenState as PokemonScreenState.Success).pokemon.forEach {
                    PokemonCell(it, navController)
                    }
                }
        }
    }

    if(endReached || viewModel.currentPage == 0){
        LaunchedEffect(viewModel){
            viewModel.getPokemons()
        }
    }
}

sealed class NavItem(val title: String, val icon: ImageVector) {
    object Search : NavItem("Pokémon", Icons.Filled.Search)
    object Home : NavItem("Search", Icons.Filled.Home)
    object Profile : NavItem("Team", Icons.Filled.Person)
}