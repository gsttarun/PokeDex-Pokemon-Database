package com.test.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.test.pokedex.adapters.PokemonListAdapter;
import com.test.pokedex.network.ApiConstants;
import com.test.pokedex.network.PokeApiService;
import com.test.pokedex.network.models.pokemon_list.PokemonItem;
import com.test.pokedex.view_model.PokemonListViewModel;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class PokemonListActivity extends AppCompatActivity implements PokemonListAdapter.OnItemClickListener, PokemonListAdapter.OnLoadMoreListener, View.OnClickListener {

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

        PokeApplication.Companion.appComponent()
                .injectPokemonListActivity(this);

        pokemonListViewModel = ViewModelProviders.of(this).get(PokemonListViewModel.class);
        pokemonListViewModel.getMutablePokemonList().observe(this, new Observer<List<PokemonItem>>() {
            @Override
            public void onChanged(@Nullable List<PokemonItem> pokemonItems) {

                if (pokemonItems == null) {
                    hideLoadingAnimation();
                    hideLoadingTextGroup();
                    showErrorConnectionGroup();
                }
                else {
                    pokemonListAdapter.setLoaded();
                    pokemonListAdapter.updateList(pokemonItems);
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
        recyclerView = findViewById(R.id.recyclerView);
        connectionErrorGroup = findViewById(R.id.connectionErrorGroup);
        loadingTextGroup = findViewById(R.id.loadingGroup);
        loadingAnimationView = findViewById(R.id.loadingLottieView);
        noConnectionAnimationView = findViewById(R.id.noConnectionLottieView);
        whiteBackgroundView = findViewById(R.id.whiteBackgroundView);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(PokemonListActivity.this, RecyclerView.VERTICAL, false);
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
        Intent intent = new Intent(PokemonListActivity.this, PokemonDetailActivity2.class);
        List<PokemonItem> pokemonItemList = pokemonListViewModel.getMutablePokemonList().getValue();
        intent.putExtra(ApiConstants.URL, pokemonItemList.get(position).getUrl());
        intent.putExtra(ApiConstants.POKEMON_NAME, pokemonItemList.get(position).getName());
        intent.putExtra(ApiConstants.POKEMON_ID, position + 1);
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
