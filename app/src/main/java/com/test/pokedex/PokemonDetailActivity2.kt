package com.test.pokedex

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.test.pokedex.network.ApiConstants
import com.test.pokedex.network.models.pokedex.pokemon2.Pokemon
import kotlinx.android.synthetic.main.activity_pokemon_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class PokemonDetailActivity2 : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    @Inject
    lateinit var imageLoader: ImageLoader

    private var pokemonUrl: String = ""
    private var pokemonName: String = ""
    private var pokemonId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        PokeApplication.appComponent()!!.injectPokemonDetailActivity(this)

        getBundleData()

        launch {
            Repository.getPokemonByNameFromDatabase(pokemonName).observe(this@PokemonDetailActivity2, Observer {
                showPokemon(it)
            })
        }
    }

    private fun getBundleData() {
        pokemonUrl = intent.getStringExtra(ApiConstants.URL)
        pokemonName = intent.getStringExtra(ApiConstants.POKEMON_NAME)
        pokemonId = intent.getIntExtra(ApiConstants.POKEMON_ID, 0)
    }

    private fun showPokemon(pokemon: Pokemon) {
        hideAnimationView()
        //        String img_url = pokemon2.getSprites().getFrontDefault();
        val imageUrl = "https://img.pokemondb.net/artwork/" + pokemon.name + ".jpg"

        Timber.v("Image URL = $imageUrl")

        imageLoader.loadImage(imageUrl, pokemon_img, img_loading_view)
        pokemon_name.text = pokemon.name
        pokemon_id.text = String.format("#%s", pokemon.id)
    }


    private fun hideAnimationView() {
        loading_view.cancelAnimation()
        loading_view.visibility = View.GONE
    }
}
