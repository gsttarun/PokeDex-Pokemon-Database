package com.test.pokedex.injection.components


import com.squareup.picasso.Picasso
import com.test.pokedex.PokemonDetailActivity
import com.test.pokedex.PokemonDetailActivity2
import com.test.pokedex.PokemonListActivity
import com.test.pokedex.PokemonListActivity2
import com.test.pokedex.adapters.PokemonListAdapter
import com.test.pokedex.injection.modules.ImageLoaderModule
import com.test.pokedex.injection.modules.PokeApiServiceModule
import com.test.pokedex.view_model.Pokemon2ViewModel
import com.test.pokedex.view_model.PokemonListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ImageLoaderModule::class, PokeApiServiceModule::class])
interface AppComponent {

    val picasso: Picasso

    fun injectPokemonListActivity(pokemonListActivity: PokemonListActivity)

    fun injectPokemonListActivity(pokemonListActivity: PokemonListActivity2)

    fun injectPokemonDetailActivity(pokemonDetailActivity: PokemonDetailActivity)

    fun injectPokemonDetailActivity(pokemonDetailActivity: PokemonDetailActivity2)

    fun injectPokemonListAdapter(pokemonListAdapter: PokemonListAdapter)

    fun injectPokemonViewModel(pokemon2ViewModel: Pokemon2ViewModel)

    fun injectPokemonListViewModel(pokemonListViewModel: PokemonListViewModel)
}
