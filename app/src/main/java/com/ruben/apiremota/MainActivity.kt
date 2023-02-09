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
import com.ruben.apiremota.data.local.PokemonRemoteDatasource
import com.ruben.apiremota.data.local.RetrofitBuilder
import com.ruben.apiremota.ui.components.MainScreen
import com.ruben.apiremota.presentation.PokemonViewModel
import com.ruben.apiremota.ui.theme.ApiRemotaTheme

class MainActivity : ComponentActivity() {
    private val apiDatasource = PokemonRemoteDatasource(RetrofitBuilder.apiService)
    private val viewModel = PokemonViewModel( apiDatasource )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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