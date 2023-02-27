package com.ruben.apiremota.data.remote

import com.ruben.apiremota.data.local.FlavorTextEntry


data class Pokemon(
    val base_experience: Int,
    val height: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val weight: Int,
    var flavorTextEntry: String
)