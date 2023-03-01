package com.ruben.apiremota

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ruben.apiremota.data.PokemonRepository
import com.ruben.apiremota.data.local.PokemonDatasource
import com.ruben.apiremota.data.local.RollsDatasource
import com.ruben.apiremota.data.remote.Pokemon
import com.ruben.apiremota.data.remote.PokemonRemoteDatasource
import com.ruben.apiremota.data.remote.RetrofitBuilder
import com.ruben.apiremota.navigation.AppScreens
import com.ruben.apiremota.presentation.PokemonViewModel
import com.ruben.apiremota.presentation.RollsViewModel
import com.ruben.apiremota.ui.screens.DetailScreen
import com.ruben.apiremota.ui.screens.RollsScreenState
import com.ruben.apiremota.ui.screens.SearchScreen
class MainActivity : ComponentActivity() {
    companion object {
        var index: Int = 1
        var rolls: Int = 0
        var pokemon: Pokemon? = null
    }

    @OptIn(ExperimentalLifecycleComposeApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiDatasource = PokemonRemoteDatasource(RetrofitBuilder.apiService)
        val localDatasource = PokemonDatasource(applicationContext)
        val rollsDatasource = RollsDatasource(applicationContext)
        val repository = PokemonRepository(localDatasource, apiDatasource, rollsDatasource)
        val viewModel = PokemonViewModel(repository)
        val rollsViewModel = RollsViewModel(repository = repository)

        rollsViewModel.getRolls()
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = AppScreens.Search.route
            ) {
                composable(AppScreens.Search.route) {
                    val rollsScreenState by rollsViewModel.uiState.collectAsStateWithLifecycle()
                    when(rollsScreenState){
                        is RollsScreenState.Success -> {
                            rolls = if ((rollsScreenState as RollsScreenState.Success).rolls == 0){
                                5
                            } else {
                                (rollsScreenState as RollsScreenState.Success).rolls
                            }
                        }
                        else -> {}
                    }
                    SearchScreen(viewModel, navController, index, repository = repository)
                }
                composable(
                    route = "detail/{id}/{name}/{height}/{weight}/{description}",
                    arguments = listOf(
                        navArgument("id") { type = NavType.IntType },
                        navArgument("name") { type = NavType.StringType },
                        navArgument("height") { type = NavType.IntType },
                        navArgument("weight") { type = NavType.IntType },
                        navArgument("description") { type = NavType.StringType },

                        )
                ) {
                    val id = it.arguments?.getInt("id")
                    requireNotNull(id)
                    val name = it.arguments?.getString("name")
                    requireNotNull(name)
                    val height = it.arguments?.getInt("height")
                    requireNotNull(height)
                    val weight = it.arguments?.getInt("weight")
                    requireNotNull(weight)
                    val description = it.arguments?.getString("description")
                    requireNotNull(description)

                    DetailScreen(id, name, height, weight, description)
                }
            }
        }
    }
}