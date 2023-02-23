package com.ruben.apiremota.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DetailScreen(id: Int , name: String , height: Int, weight: Int ) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column() {
            Text(text = "DETALLES DEL POKEMON")
            Text(text = "Name: $name")
            Text(text = "Pokemon: $id")
            Text(text = "Height: $height")
            Text(text = "Weight: $weight")
        }
    }
}