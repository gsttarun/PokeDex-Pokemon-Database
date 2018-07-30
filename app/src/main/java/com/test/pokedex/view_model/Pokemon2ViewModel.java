package com.test.pokedex.view_model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.test.pokedex.PokeApplication;
import com.test.pokedex.network.PokedexApiService;
import com.test.pokedex.network.models.pokedex.pokemon2.Pokemon2;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pokemon2ViewModel extends ViewModel {

    @Inject
    PokedexApiService pokedexApiService; //cannot be private cause of dagger injection

    private MutableLiveData<Pokemon2> pokemon2;

    public Pokemon2ViewModel() {
        super();
        PokeApplication.appComponent().injectPokemonViewModel(this);
    }



    public MutableLiveData<Pokemon2> getPokemon2(int pokemonId) {

        if (pokemon2 == null) {
            pokemon2 = new MutableLiveData<Pokemon2>();
            loadPokemon(pokemonId);
        }
        return pokemon2;
    }

    private void loadPokemon(int pokemonId) {
        Call<List<Pokemon2>> pokemonByID = pokedexApiService.getPokemonByID(pokemonId);
        pokemonByID.enqueue(callback);
    }

    private Callback<List<Pokemon2>> callback = new Callback<List<Pokemon2>>() {
        @Override
        public void onResponse(Call<List<Pokemon2>> call, Response<List<Pokemon2>> response) {
            if (response.body() != null) {
                pokemon2.postValue(response.body().get(0));
            }
            else {// TODO: 4/6/18 show error
            }
        }

        @Override
        public void onFailure(Call<List<Pokemon2>> call, Throwable t) {
        }
    };

}
