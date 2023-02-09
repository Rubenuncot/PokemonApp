package com.ruben.apiremota.data.local

import android.content.Context

class PokemonDatasource constructor(private val applicationContext: Context) {

    suspend fun getPokemon(offset: Int): List<PokemonEntity> {
        val db = getDatabase(applicationContext)
        return db.pokemonDao().getAll(offset);
    }

    suspend fun createPokemon(base_experience: Int, height: Int, name: String, order: Int, weight: Int) {
        val db = getDatabase(applicationContext)
        db.pokemonDao().insert(PokemonEntity(base_experience = base_experience, height = height, name = name, order = order, weight = weight))
    }

    suspend fun createAllPokemon(pokemons: List<PokemonEntity>){
        val db = getDatabase(applicationContext)
        db.pokemonDao().insertAll(pokemons)
    }
    suspend fun deletePokemon(pokemon: PokemonEntity) {
        val db = getDatabase(applicationContext)
        db.pokemonDao().delete(pokemon)
    }
}