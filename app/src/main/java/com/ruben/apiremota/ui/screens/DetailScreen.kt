package com.ruben.apiremota.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun DetailScreen(id: Int , name: String , height: Int, weight: Int, description: String) {
    val offset = Offset(3.0f,8.0f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.TopCenter
    ){
        Column(Modifier.padding(20.dp)) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(20)),
                shape = RoundedCornerShape(20),
                backgroundColor = Color(0xFFECE3F6)
            ) {
                Column() {
                    Text(text = "Pokemon Details",modifier = Modifier.width(350.dp), style = TextStyle(textAlign = TextAlign.Center,fontSize = 35.sp,fontWeight = FontWeight.Bold,color = Color.Black, shadow = Shadow(
                        color = Color.White, offset = offset, blurRadius = 2f
                    )))
                    Text(text = "$name".uppercase(),textAlign = TextAlign.Center,modifier = Modifier.width(350.dp), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    AsyncImage(model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
                        contentDescription = "Image pokemon : $id",modifier = Modifier.width(350.dp))
                    Row (
                        horizontalArrangement = Arrangement.Center
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(text = "Pokedex Number #: $id", style = TextStyle(textAlign = TextAlign.Center))
                            Text(text = "Height: $height", style = TextStyle(textAlign = TextAlign.Center))
                            Text(text = "Weight: $weight", style = TextStyle(textAlign = TextAlign.Center))
                            Text(text = "Description: $description", style = TextStyle(textAlign = TextAlign.Center))
                        }
                    }

                }

            }
        }
    }
}