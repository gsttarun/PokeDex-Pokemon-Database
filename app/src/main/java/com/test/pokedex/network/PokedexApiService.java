package com.test.pokedex.network;


import com.test.pokedex.network.models.pokedex.pokemon2.Pokemon2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static com.test.pokedex.network.ApiConstants.POKEMON_ID;
import static com.test.pokedex.network.ApiConstants.POKEMON_NAME;


public interface PokedexApiService {

    @GET("pokemon/{pokemon_id}")
    Call<List<Pokemon2>> getPokemonByID(@Path(POKEMON_ID) int pokemonId);

    @GET("pokemon/{pokemon_name}")
    Call<List<Pokemon2>> getPokemonByName(@Path(POKEMON_NAME) String pokemonName);
}
