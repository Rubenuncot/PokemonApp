package com.ruben.apiremota

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import com.ruben.apiremota.ui.screens.DetailScreen
import com.ruben.apiremota.ui.screens.SearchScreen
class MainActivity : ComponentActivity() {
    companion object {
        var index: Int = 1
        var rolls: Int = 0
        var pokemon: Pokemon? = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiDatasource = PokemonRemoteDatasource(RetrofitBuilder.apiService)
        val localDatasource = PokemonDatasource(applicationContext)
        val rollsDatasource = RollsDatasource(applicationContext)
        val repository = PokemonRepository(localDatasource, apiDatasource, rollsDatasource)
        val viewModel = PokemonViewModel(repository)

        setContent {
            val navController = rememberNavController()
            rolls = viewModel.getRolls()
            rolls = if (rolls == 0){
                5
            } else {
                viewModel.getRolls()
            }

            NavHost(
                navController = navController,
                startDestination = AppScreens.Search.route
            ) {
                composable(AppScreens.Search.route) {
                    SearchScreen(viewModel, navController, index)
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