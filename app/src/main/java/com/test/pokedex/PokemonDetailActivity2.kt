package com.test.pokedex

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.test.pokedex.network.ApiConstants
import com.test.pokedex.network.models.pokedex.pokemon2.Pokemon
import com.test.pokedex.view_model.Pokemon2ViewModel
import kotlinx.android.synthetic.main.activity_pokemon_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class PokemonDetailActivity2 : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    @Inject
    public lateinit var imageLoader: ImageLoader

    private var pokemonUrl: String = ""
    private var pokemonName: String = ""
    private var pokemonId: Int = 0

    private var pokemon: Pokemon? = null
    private var pokemon2List: List<Pokemon>? = null
    private val networkObserver = Observer<Resource<Pokemon>> { responseResource ->
        when (responseResource.status) {
            Status.SUCCESS -> {
                responseResource.data?.let {
                    launch {
                        Repository.savePokemon(it)
                    }
                }
            }
            Status.ERROR -> {

            }
        }
    }

    internal var imageRoot = "https://img.pokemondb.net/artwork/"
    internal var imageRoot2 = "https://pokeres.bastionbot.org/images/pokemon/"

    internal var callback: Callback<Pokemon> = object : Callback<Pokemon> {
        override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
            pokemon = response.body()
            hideAnimationView()
            setPokemonData()
        }

        override fun onFailure(call: Call<Pokemon>, t: Throwable) {

        }
    }

    internal var pokemon2Callback: Callback<List<Pokemon>> = object : Callback<List<Pokemon>> {
        override fun onResponse(call: Call<List<Pokemon>>, response: Response<List<Pokemon>>) {
            if (response.body() != null) {
                pokemon2List = response.body()
                setPokemon2Data()
            } else {
                // TODO: 4/6/18 show error
            }
        }

        override fun onFailure(call: Call<List<Pokemon>>, t: Throwable) {}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        PokeApplication.appComponent()!!.injectPokemonDetailActivity(this)

        getBundleData()

        //        Call<Pokemon> pokemonByUrlCall = pokeApiService.getPokemonByURL(pokemonUrl);
        //        pokemonByUrlCall.enqueue(callback);

        //        Call<List<Pokemon>> pokemonByID = pokedexApiService.getPokemonByID(pokemonId);
        //        pokemonByID.enqueue(pokemon2Callback);

        val pokemon2ViewModel = ViewModelProviders.of(this).get(Pokemon2ViewModel::class.java)
        /* pokemon2ViewModel.getPokemon2(pokemonId).observe(this, Observer { pokemon2 ->
             if (pokemon2 != null) {
                 showPokemon2(pokemon2)
             }
         })*/
        launch {
            Repository.getPokemonByIdFromDatabase(pokemonId).observe(this@PokemonDetailActivity2, Observer {
                if (it == null) {
                    Repository.getPokemonByIdFromNetwork(pokemonId).observe(this@PokemonDetailActivity2, networkObserver)
                } else
                    showPokemon2(it)
            })
        }
    }

    private fun getBundleData() {
        pokemonUrl = intent.getStringExtra(ApiConstants.URL)
        pokemonName = intent.getStringExtra(ApiConstants.POKEMON_NAME)
        pokemonId = intent.getIntExtra(ApiConstants.POKEMON_ID, 0)
    }

    private fun setPokemon2Data() {
        val pokemon2 = pokemon2List!![0]
        showPokemon2(pokemon2)
    }

    private fun showPokemon2(pokemon2: Pokemon) {
        hideAnimationView()
        //        String img_url = pokemon2.getSprites().getFrontDefault();
        val imageUrl = "https://img.pokemondb.net/artwork/" + pokemon2.name + ".jpg"

        Timber.v("Image URL = $imageUrl")

        imageLoader!!.loadImage(imageUrl, pokemon_img, img_loading_view)
        pokemon_name.text = pokemon2.name
        pokemon_id.text = String.format("#%s", pokemon2.id)
    }


    private fun hideAnimationView() {
        loading_view.cancelAnimation()
        loading_view.visibility = View.GONE
    }

    private fun setPokemonData() {
        val imageUrl: String = imageRoot2 + pokemon!!.id + ".png"
        //        img_url = imageRoot + pokemon.getName() + ".jpg";

        Timber.v("Image URL = $imageUrl")

        imageLoader!!.loadImage(imageUrl, pokemon_img, img_loading_view)

        pokemon_name.text = pokemon!!.name
        pokemon_id.text = String.format("# %d", pokemon!!.id)

    }

}
