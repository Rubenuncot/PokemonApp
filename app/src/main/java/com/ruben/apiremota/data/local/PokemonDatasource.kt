package com.ruben.apiremota.data.local

import android.content.Context
import com.ruben.apiremota.data.remote.Pokemon

class PokemonDatasource constructor(private val applicationContext: Context) {

    suspend fun getPokemons(offset: Int): List<PokemonEntity> {
        val db = getDatabase(applicationContext)
        return db.pokemonDao().getAll(offset)
    }

    suspend fun createPokemon(pokemon: Pokemon) {
        val db = getDatabase(applicationContext)
        db.pokemonDao().insert(PokemonEntity(id = pokemon.id, base_experience = pokemon.base_experience, height = pokemon.height, name = pokemon.name, order = pokemon.order, weight = pokemon.weight))
    }

    suspend fun createAllPokemons(pokemons: List<PokemonEntity>){
        val db = getDatabase(applicationContext)
        db.pokemonDao().insertAll(pokemons)
    }
    suspend fun deletePokemon(pokemon: PokemonEntity) {
        val db = getDatabase(applicationContext)
        db.pokemonDao().delete(pokemon)
    }

    suspend fun getPokemon(id: Int): PokemonEntity{
        val db = getDatabase(applicationContext)
        return db.pokemonDao().getPokemon(id)
    }
}