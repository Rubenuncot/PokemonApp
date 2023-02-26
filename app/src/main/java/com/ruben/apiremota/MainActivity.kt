package com.ruben.apiremota

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ruben.apiremota.data.PokemonRepository
import com.ruben.apiremota.data.local.PokemonDatasource
import com.ruben.apiremota.data.remote.PokemonRemoteDatasource
import com.ruben.apiremota.data.remote.RetrofitBuilder
import com.ruben.apiremota.navigation.AppScreens
import com.ruben.apiremota.presentation.PokemonViewModel
import com.ruben.apiremota.ui.screens.DetailScreen
import com.ruben.apiremota.ui.screens.SearchScreen

class MainActivity : ComponentActivity() {
    companion object {
        var index: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiDatasource = PokemonRemoteDatasource(RetrofitBuilder.apiService)
        val localDatasource = PokemonDatasource(applicationContext)
        val repository = PokemonRepository(localDatasource, apiDatasource)
        val viewModel = PokemonViewModel(repository)



        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = AppScreens.Search.route
            ) {
                composable(AppScreens.Search.route) {
                    SearchScreen(viewModel, navController, index)
                }
                composable(
                    route = "detail/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) {
                    val id = it.arguments?.getInt("id")
                    requireNotNull(id)
                    DetailScreen(id)
                }
            }
        }
    }
}