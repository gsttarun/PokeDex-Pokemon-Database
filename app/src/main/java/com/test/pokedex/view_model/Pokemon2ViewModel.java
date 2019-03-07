package com.test.pokedex.view_model;

import com.test.pokedex.PokeApplication;
import com.test.pokedex.network.PokeApiService;
import com.test.pokedex.network.PokedexApiService;
import com.test.pokedex.network.models.pokedex.pokemon2.Pokemon;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pokemon2ViewModel extends ViewModel {

    @Inject
    PokedexApiService pokedexApiService; //cannot be private cause of dagger injection

    @Inject
    PokeApiService pokeApiService;

    private MutableLiveData<Pokemon> pokemon2;

    public Pokemon2ViewModel() {
        super();
        PokeApplication.Companion.appComponent().injectPokemonViewModel(this);
    }


    public MutableLiveData<Pokemon> getPokemon2(int pokemonId) {

        if (pokemon2 == null) {
            pokemon2 = new MutableLiveData<Pokemon>();
            loadPokemon(pokemonId);
        }
        return pokemon2;
    }

    private void loadPokemon(int pokemonId) {
        Call<Pokemon> pokemonByID = pokeApiService.getPokemonByID(pokemonId);
        pokemonByID.enqueue(callback);
    }

    private Callback<Pokemon> callback = new Callback<Pokemon>() {
        @Override
        public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
            if (response.body() != null) {
                pokemon2.postValue(response.body());
            }
            else {// TODO: 4/6/18 show error
            }
        }

        @Override
        public void onFailure(Call<Pokemon> call, Throwable t) {
        }
    };

}
