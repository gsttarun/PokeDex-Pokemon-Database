package com.test.pokedex.view_model;


import com.test.pokedex.PokeApplication;
import com.test.pokedex.network.PokeApiService;
import com.test.pokedex.network.models.pokemon_list.PokemonListResponse;
import com.test.pokedex.network.models.pokemon_list.PokemonItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class PokemonListViewModel extends ViewModel {

    @Inject
    PokeApiService pokeApiService;

    private List<PokemonItem> pokemonList = new ArrayList<>();
    private MutableLiveData<List<PokemonItem>> mutablePokemonList;
    private String nextUrl = "";

    public PokemonListViewModel() {
        super();
        PokeApplication.Companion.appComponent().injectPokemonListViewModel(this);
    }

    public List<PokemonItem> getPokemonList() {
        return pokemonList;
    }

    private Callback<PokemonListResponse> callback = new Callback<PokemonListResponse>() {
        @Override
        public void onResponse(Call<PokemonListResponse> call, Response<PokemonListResponse> response) {
            if (response.body() != null) {
                if (response.body() != null) {
                    List<PokemonItem> pokemonItems = response.body().getPokemonItems();
                    pokemonList.addAll(pokemonItems);
                    mutablePokemonList.postValue(pokemonItems);
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

    public MutableLiveData<List<PokemonItem>> getMutablePokemonList() {
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

//                List<PokemonItem> pokemonItems = response.body().getPokemonItems();
//                pokemonList.addAll(pokemonItems);
                List<PokemonItem> pokemonItems = mutablePokemonList.getValue();
                pokemonItems.addAll(response.body().getPokemonItems());

                mutablePokemonList.postValue(pokemonItems);
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
