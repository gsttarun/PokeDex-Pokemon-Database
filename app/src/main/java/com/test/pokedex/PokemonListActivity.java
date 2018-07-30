package com.test.pokedex;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.test.pokedex.adapters.PokemonListAdapter;
import com.test.pokedex.network.ApiConstants;
import com.test.pokedex.network.PokeApiService;
import com.test.pokedex.network.models.pokemon_list.Result;
import com.test.pokedex.view_model.PokemonListViewModel;

import java.util.List;

import javax.inject.Inject;


public class PokemonListActivity extends AppCompatActivity implements PokemonListAdapter.OnItemClickListener, PokemonListAdapter.OnLoadMoreListener, View.OnClickListener, ApiConstants {

    @Inject
    PokeApiService pokeApiService;

    @Inject
    PokemonListAdapter pokemonListAdapter;

    PokemonListViewModel pokemonListViewModel;

    RecyclerView recyclerView;
    Group connectionErrorGroup, loadingTextGroup;
    LottieAnimationView loadingAnimationView, noConnectionAnimationView;
    View whiteBackgroundView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pokemon_list);

        PokeApplication.appComponent()
                .injectPokemonListActivity(this);

        pokemonListViewModel = ViewModelProviders.of(this).get(PokemonListViewModel.class);
        pokemonListViewModel.getMutablePokemonList().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> results) {

                if (results == null) {
                    hideLoadingAnimation();
                    hideLoadingTextGroup();
                    showErrorConnectionGroup();
                }
                else {
                    pokemonListAdapter.setLoaded();
                    pokemonListAdapter.updateList(results);
                    hideErrorConnectionGroup();
                    hideLoadingAnimation();
                    hideLoadingTextGroup();
                }
            }
        });

        initViews();

        setListeners();

        setUpRecyclerView();

        setUpAdapter();

        playLoadingAnimation();
        showLoadingTextGroup();
//        hideErrorConnectionGroup();
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
        noConnectionAnimationView = findViewById(R.id.no_connection_lottie_view);
        whiteBackgroundView = findViewById(R.id.white_background_view);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(PokemonListActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(pokemonListAdapter);
    }


    private void showErrorConnectionGroup() {
        connectionErrorGroup.setVisibility(View.VISIBLE);
        loadingAnimationView.playAnimation();
        whiteBackgroundView.setClickable(true);
        whiteBackgroundView.setFocusableInTouchMode(true);
    }

    private void hideErrorConnectionGroup() {
        whiteBackgroundView.setClickable(false);
        whiteBackgroundView.setFocusableInTouchMode(false);
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


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(PokemonListActivity.this, PokemonDetailActivity.class);
        List<Result> resultList = pokemonListViewModel.getMutablePokemonList().getValue();
        intent.putExtra(URL, resultList.get(position).getUrl());
        intent.putExtra(POKEMON_NAME, resultList.get(position).getName());
        intent.putExtra(POKEMON_ID, position + 1);
        startActivity(intent);
    }

    @Override
    public void onLoadMore() {
        pokemonListViewModel.loadMorePokemons();
    }

    @Override
    public void onClick(View v) {
        hideErrorConnectionGroup();
        showLoadingTextGroup();
        playLoadingAnimation();
        pokemonListViewModel.loadInitialPokemonList();
    }
}
