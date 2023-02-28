package com.ruben.apiremota.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ruben.apiremota.MainActivity
import com.ruben.apiremota.R
import com.ruben.apiremota.data.remote.Pokemon
import com.ruben.apiremota.presentation.PokemonViewModel

var pokeRandom: Pokemon? = null


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GuessScreen (id: Int, name: String, pokemon: Pokemon?, viewModel: PokemonViewModel){
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
                    Text(
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
                    viewModel.getRandomPokemon()
                    if (pokemon != null) {
                        AsyncImage(
                            model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + pokemon.id + ".png",
                            modifier = Modifier.width(350.dp)
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
                    ) {
                        TextField(
                            value = pokeName.value,
                            onValueChange = { pokeName.value = it },
                            label = { Text("Enter the name of the pokemon") },
                            placeholder = { Text("Pokemon Name") }
                        )
                        Box(){
                            Button(onClick = {
                                             if (pokeName.equals("$name")){
                                                 MainActivity.rolls+=1
                                             }
                            },
                                shape = RoundedCornerShape(100),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.Black,
                                    containerColor = Color(0xFFECE3F6)
                                ),
                                modifier = Modifier.shadow(elevation = 10.dp, shape = RoundedCornerShape(100)),)
                            {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "CHECK",
                                        modifier = Modifier.size(50.dp)
                                    )
                                }

                            }
                        }
                    }
                }
            }}}}

