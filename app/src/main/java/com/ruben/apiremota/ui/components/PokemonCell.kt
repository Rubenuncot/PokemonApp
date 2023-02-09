package com.ruben.apiremota.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ruben.apiremota.data.local.PokemonEntity

@Composable
fun PokemonCell (pokemon: PokemonEntity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(15.dp)) {
            Text(text = "Nombre: " + pokemon.name, fontWeight = FontWeight. Bold)
            Text(text = "Experiencia base: " +pokemon.base_experience.toString())
            Text(text = "Número de pokedex: " + pokemon.id.toString(), style = TextStyle(color = Color.LightGray, textAlign = TextAlign.Center ))
        }
    }
}