package com.test.pokedex.network;


import com.test.pokedex.network.models.pokemon.Pokemon1;
import com.test.pokedex.network.models.pokemon_list.PokemonListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

import static com.test.pokedex.network.ApiConstants.LIMIT;
import static com.test.pokedex.network.ApiConstants.OFFSET;
import static com.test.pokedex.network.ApiConstants.POKEMON_ID;
import static com.test.pokedex.network.ApiConstants.POKEMON_NAME;


public interface PokeApiService {

    @GET("pokemon")
    Call<PokemonListResponse> getPokemonList(@Query(LIMIT) int limit, @Query(OFFSET) int offset);

    @GET("pokemon")
    Call<PokemonListResponse> getPokemonList();

    @GET
    Call<PokemonListResponse> getPokemonListByURL(@Url String nextUrl);

    @GET("pokemon/{pokemon_name}")
    Call<Pokemon1> getPokemonByName(@Path(POKEMON_NAME) String pokemonName);

    @GET("pokemon/{pokemon_id}")
    Call<Pokemon1> getPokemonByID(@Path(POKEMON_ID) String pokemonId);

    @GET
    Call<Pokemon1> getPokemonByURL(@Url String pokemonUrl);
}
