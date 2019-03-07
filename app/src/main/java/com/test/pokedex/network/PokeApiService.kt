package com.test.pokedex.network


import com.test.pokedex.BuildConfig
import com.test.pokedex.network.ApiConstants.LIMIT
import com.test.pokedex.network.ApiConstants.OFFSET
import com.test.pokedex.network.ApiConstants.POKEMON_ID
import com.test.pokedex.network.ApiConstants.POKEMON_NAME
import com.test.pokedex.network.models.pokedex.pokemon2.Pokemon
import com.test.pokedex.network.models.pokemon_list.PokemonListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface PokeApiService {

    @GET("pokemon")
    fun getPokemonList(): Call<PokemonListResponse>

    @GET("pokemon")
    fun getPokemonList(
            @Query(LIMIT) limit: Int,
            @Query(OFFSET) offset: Int
    ): Call<PokemonListResponse>

    @GET
    fun getPokemonListByURL(
            @Url nextUrl: String
    ): Call<PokemonListResponse>

    @GET("pokemon/{pokemon_name}")
    fun getPokemonByName(
            @Path(POKEMON_NAME) pokemonName: String
    ): Call<Pokemon>

    @GET("pokemon/{pokemon_id}")
    fun getPokemonByID(
            @Path(POKEMON_ID) pokemonId: Int
    ): Call<Pokemon>

    @GET
    fun getPokemonByURL(
            @Url pokemonUrl: String
    ): Call<Pokemon>
}

val pokeApiService by lazy {
    BaseApiService.create<PokeApiService>(BuildConfig.POKE_API_BASE)
}
