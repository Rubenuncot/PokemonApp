package com.ruben.apiremota.data.remote

import com.ruben.apiremota.data.local.Species
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("pokemon")
    suspend fun getPokemons(@Query("offset") offset: Int, @Query("limit") limit: Int): PageResponse
    @GET("pokemon/{id}/")
    suspend fun getPokemon(@Path("id") id: Int): Pokemon

    @GET("pokemon-species/{id}")
    suspend fun getSpecie(@Path("id") id :Int): Species
}
