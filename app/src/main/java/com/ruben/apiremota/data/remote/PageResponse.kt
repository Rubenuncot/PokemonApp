package com.ruben.apiremota.data.remote

import com.ruben.apiremota.data.local.PokemonEntity as LocalPokemon

data class PageResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)

data class Result(
    val name: String,
    val url: String
)

fun List<Pokemon>.toLocalEntity() = map {
    LocalPokemon(
        id = it.id,
        base_experience = it.base_experience,
        height = it.height,
        weight = it.weight,
        name = it.name,
        order = it.order,
        flavorTextEntry = it.flavorTextEntry
    )
}