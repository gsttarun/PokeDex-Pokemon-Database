package com.test.pokedex.injection.components;


import com.squareup.picasso.Picasso;
import com.test.pokedex.PokemonDetailActivity;
import com.test.pokedex.PokemonDetailActivity2;
import com.test.pokedex.PokemonListActivity;
import com.test.pokedex.adapters.PokemonListAdapter;
import com.test.pokedex.injection.modules.ImageLoaderModule;
import com.test.pokedex.injection.modules.PokeApiServiceModule;
import com.test.pokedex.view_model.Pokemon2ViewModel;
import com.test.pokedex.view_model.PokemonListViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ImageLoaderModule.class, PokeApiServiceModule.class})
public interface AppComponent {

    void injectPokemonListActivity(PokemonListActivity pokemonListActivity);

    void injectPokemonDetailActivity(PokemonDetailActivity pokemonDetailActivity);

    void injectPokemonDetailActivity(PokemonDetailActivity2 pokemonDetailActivity);

    void injectPokemonListAdapter(PokemonListAdapter pokemonListAdapter);

    void injectPokemonViewModel(Pokemon2ViewModel pokemon2ViewModel);

    void injectPokemonListViewModel(PokemonListViewModel pokemonListViewModel);

    Picasso getPicasso();
}
