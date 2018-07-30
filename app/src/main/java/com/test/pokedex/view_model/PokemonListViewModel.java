package com.test.pokedex.view_model;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.test.pokedex.PokeApplication;
import com.test.pokedex.network.PokeApiService;
import com.test.pokedex.network.models.pokemon_list.PokemonListResponse;
import com.test.pokedex.network.models.pokemon_list.Result;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class PokemonListViewModel extends ViewModel {

    @Inject
    PokeApiService pokeApiService;

    private List<Result> pokemonList = new ArrayList<>();
    private MutableLiveData<List<Result>> mutablePokemonList;
    private String nextUrl = "";

    public PokemonListViewModel() {
        super();
        PokeApplication.appComponent().injectPokemonListViewModel(this);
    }

    public List<Result> getPokemonList() {
        return pokemonList;
    }

    private Callback<PokemonListResponse> callback = new Callback<PokemonListResponse>() {
        @Override
        public void onResponse(Call<PokemonListResponse> call, Response<PokemonListResponse> response) {
            if (response.body() != null) {
                if (response.body() != null) {
                    List<Result> results = response.body().getResults();
                    pokemonList.addAll(results);
                    mutablePokemonList.postValue(results);
                    nextUrl = response.body().getNext();
                }
            }
        }

        @Override
        public void onFailure(Call<PokemonListResponse> call, Throwable t) {
            mutablePokemonList.postValue(null);
        }
    };

    public void loadInitialPokemonList() {
        Call<PokemonListResponse> pokemonList = pokeApiService.getPokemonList();
        pokemonList.enqueue(callback);
    }

    public MutableLiveData<List<Result>> getMutablePokemonList() {
        if (mutablePokemonList == null) {
            mutablePokemonList = new MutableLiveData<>();
            loadInitialPokemonList();
        }
//        else if (pokemonList.size() > 0)
//            mutablePokemonList.setValue(pokemonList);
        return mutablePokemonList;
    }

    public void loadMorePokemons() {

        //Generating more data
        Timber.v("loadMore = " + nextUrl);
        Call<PokemonListResponse> pokemonListByURL = pokeApiService.getPokemonListByURL(nextUrl);
        Callback<PokemonListResponse> loadMoreCallback = new Callback<PokemonListResponse>() {
            @Override
            public void onResponse(Call<PokemonListResponse> call, final Response<PokemonListResponse> response) {

//                List<Result> results = response.body().getResults();
//                pokemonList.addAll(results);
                List<Result> results = mutablePokemonList.getValue();
                results.addAll(response.body().getResults());

                mutablePokemonList.postValue(results);
                nextUrl = response.body().getNext();
            }

            @Override
            public void onFailure(Call<PokemonListResponse> call, Throwable t) {
                Timber.e("getPokemonList API fail :" + t.getMessage());
                mutablePokemonList.postValue(mutablePokemonList.getValue());
            }
        };
        pokemonListByURL.enqueue(loadMoreCallback);
    }

}
