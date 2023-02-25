package com.ruben.apiremota.ui.components

import android.media.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun DetailScreen(id: Int , name: String , height: Int, weight: Int) {
    val offset = Offset(3.0f,8.0f)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){
        Column(Modifier.padding(20.dp)) {
            Text(text = "Pokemon Details", style = TextStyle(fontSize = 33.sp, shadow = Shadow(
                color = Color.LightGray, offset = offset, blurRadius = 2f
            )))
            Text(text = "Name: $name",textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            AsyncImage(model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
                contentDescription = "Imagen del pokemon número: $id")
            Text(text = "Id Pokemon: $id")
            Text(text = "Height: $height")
            Text(text = "Weight: $weight")
        }
    }
}