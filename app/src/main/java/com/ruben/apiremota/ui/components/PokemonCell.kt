package com.ruben.apiremota.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ruben.apiremota.data.local.PokemonEntity
import com.ruben.apiremota.ui.theme.DetailScreenConst

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PokemonCell (pokemon: PokemonEntity, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(20)),
        onClick = { navController.navigate("$DetailScreenConst/${pokemon.id}") },
        backgroundColor = Color(0xFFECE3F6),
        shape = RoundedCornerShape(20),
    ) {
        Column(
            Modifier.padding(15.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(80.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + pokemon.id + ".png",
                    contentDescription = "Imagen del pokemon número: " + pokemon.id,
                    modifier = Modifier.size(100.dp),
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(text = "Nombre: " + pokemon.name, fontWeight = FontWeight.Bold)
                    Text(text = "Experiencia base: " + pokemon.base_experience.toString())
                    Text(
                        text = "Número de pokedex: #" + pokemon.id.toString(),
                        style = TextStyle(color = Color.LightGray, textAlign = TextAlign.Center)
                    )
                }
            }
        }
    }
}