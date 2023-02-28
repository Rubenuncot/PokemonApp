package com.ruben.apiremota.ui.screens

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GuessScreen (id: Int, name: String){
    val offset = Offset(3.0f, 8.0f)
    val pokeName = remember { mutableStateOf(TextFieldValue()) }
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
                    AsyncImage(
                        model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
                        contentDescription = "Image pokemon : $id",
                        modifier = Modifier.width(350.dp)
                    )

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
                        Button(onClick = { /*TODO*/ }) {
                            
                        }
                    }
                }
            }}}}

