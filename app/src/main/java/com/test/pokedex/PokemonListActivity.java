package com.test.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.test.pokedex.adapters.PokemonListAdapter;
import com.test.pokedex.network.ApiConstants;
import com.test.pokedex.network.PokeApiService;
import com.test.pokedex.network.models.pokemon_list.PokemonListResponse;
import com.test.pokedex.network.models.pokemon_list.Result;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class PokemonListActivity extends AppCompatActivity implements PokemonListAdapter.OnItemClickListener, PokemonListAdapter.OnLoadMoreListener, View.OnClickListener, ApiConstants {

    @Inject
    PokeApiService pokeApiService;

    @Inject
    PokemonListAdapter pokemonListAdapter;

    RecyclerView recyclerView;
    private List<Result> results;
    private String nextUrl = null;
    Group connectionErrorGroup, loadingTextGroup;
    LottieAnimationView loadingAnimationView;
    View whiteBackgroundView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pokemon_list);

        PokeApplication
                .appComponent()
                .injectPokemonListActivity(this);

        initViews();

        setListeners();

        setUpRecyclerView();

        setUpAdapter();

        callPokemonListAPI();
    }

    private void setListeners() {
        whiteBackgroundView.setOnClickListener(this);
    }

    private void setUpAdapter() {
        pokemonListAdapter.setOnLoadMoreListener(this);
        pokemonListAdapter.setOnItemClickListener(this);
        pokemonListAdapter.setRecyclerView(recyclerView);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        connectionErrorGroup = findViewById(R.id.connection_error_group);
        loadingTextGroup = findViewById(R.id.loading_group);
        loadingAnimationView = findViewById(R.id.loading_lottie_view);
        whiteBackgroundView = findViewById(R.id.white_background_view);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(PokemonListActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(pokemonListAdapter);
    }

    private void callPokemonListAPI() {
        playLoadingAnimation();
        showLoadingTextGroup();

        Call<PokemonListResponse> pokemonList = pokeApiService.getPokemonList();

        pokemonList.enqueue(new Callback<PokemonListResponse>() {
            @Override
            public void onResponse(Call<PokemonListResponse> call, Response<PokemonListResponse> response) {
                if (response.body() != null) {
                    results = response.body().getResults();
                    nextUrl = response.body().getNext();
                    pokemonListAdapter.setResults(results);
                    hideLoadingTextGroup();
                    hideLoadingAnimation();
                }
            }

            @Override
            public void onFailure(Call<PokemonListResponse> call, Throwable t) {

            }
        });
    }

    private void showErrorConnectionGroup() {
        connectionErrorGroup.setVisibility(View.VISIBLE);
    }

    private void hideErrorConnectionGroup() {
        connectionErrorGroup.setVisibility(View.GONE);
    }

    private void showLoadingTextGroup() {
        loadingTextGroup.setVisibility(View.VISIBLE);
    }

    private void hideLoadingTextGroup() {
        loadingTextGroup.setVisibility(View.GONE);
    }

    private void playLoadingAnimation() {
        loadingAnimationView.setVisibility(View.VISIBLE);
        loadingAnimationView.playAnimation();
    }

    private void hideLoadingAnimation() {
        loadingAnimationView.cancelAnimation();
        loadingAnimationView.setVisibility(View.GONE);
    }

    private void loadMorePokemons() {

        //Generating more data
        Call<PokemonListResponse> pokemonListByURL = pokeApiService.getPokemonListByURL(nextUrl);
        pokemonListByURL.enqueue(new Callback<PokemonListResponse>() {
            @Override
            public void onResponse(Call<PokemonListResponse> call, final Response<PokemonListResponse> response) {
//                        results.addAll(response.body().getResults());
                pokemonListAdapter.setLoaded();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pokemonListAdapter.addResults(response.body().getResults());
                    }
                });
                nextUrl = response.body().getNext();
//                results.remove(results.size() - 1);
//                pokemonListAdapter.notifyItemRemoved(results.size());
            }

            @Override
            public void onFailure(Call<PokemonListResponse> call, Throwable t) {
                Timber.e("getPokemonList API fail :" + t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(PokemonListActivity.this, PokemonDetailActivity.class);
        intent.putExtra(URL, results.get(position).getUrl());
        intent.putExtra(POKEMON_NAME, results.get(position).getName());
        intent.putExtra(POKEMON_ID, position + 1);
        startActivity(intent);
    }

    @Override
    public void onLoadMore() {
        loadMorePokemons();
    }

    @Override
    public void onClick(View v) {
        callPokemonListAPI();
    }
}
