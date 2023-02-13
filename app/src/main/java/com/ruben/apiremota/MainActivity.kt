package com.ruben.apiremota

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.ruben.apiremota.data.PokemonRepository
import com.ruben.apiremota.data.local.PokemonDatasource
import com.ruben.apiremota.data.remote.PokemonRemoteDatasource
import com.ruben.apiremota.data.remote.RetrofitBuilder
import com.ruben.apiremota.ui.components.MainScreen
import com.ruben.apiremota.presentation.PokemonViewModel
import com.ruben.apiremota.ui.theme.ApiRemotaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiDatasource = PokemonRemoteDatasource(RetrofitBuilder.apiService)
        val localDatasource = PokemonDatasource(applicationContext)
        val repository = PokemonRepository(localDatasource, apiDatasource)
        val viewModel = PokemonViewModel( repository )


        setContent {
            val navController = rememberNavController()

            ApiRemotaTheme() {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color =
                MaterialTheme. colors.background) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ApiRemotaTheme {
        Greeting("Android")
    }
}