package com.test.pokedex

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.pokedex.adapters.PokemonListAdapter
import com.test.pokedex.network.ApiConstants
import com.test.pokedex.network.PokeApiService
import com.test.pokedex.network.models.pokemon_list.PokemonItem
import com.test.pokedex.view_model.PokemonListViewModel
import kotlinx.android.synthetic.main.activity_pokemon_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class PokemonListActivity2 : AppCompatActivity(), CoroutineScope, PokemonListAdapter.OnItemClickListener, PokemonListAdapter.OnLoadMoreListener, View.OnClickListener {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @Inject
    lateinit var pokeApiService: PokeApiService

    @Inject
    lateinit var pokemonListAdapter: PokemonListAdapter

    private val pokemonListViewModel: PokemonListViewModel by lazy {
        ViewModelProviders.of(this).get(PokemonListViewModel::class.java)
    }
    private val networkObserver = Observer<Resource<ArrayList<PokemonItem>?>> { responseResource ->
        when (responseResource.status) {
            Status.SUCCESS -> {
                responseResource.data?.let {
                    launch {
                        Repository.savePokemonItems(it)
                    }
                }
            }
            Status.ERROR -> {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pokemon_list)

        PokeApplication.appComponent()!!.injectPokemonListActivity(this)

        pokemonListViewModel.mutablePokemonList.observe(this, Observer { pokemonItems ->
            if (pokemonItems == null) {
                hideLoadingAnimation()
                hideLoadingTextGroup()
                showErrorConnectionGroup()
            } else {
                pokemonListAdapter.setLoaded()
                pokemonListAdapter.updateList(pokemonItems)
                hideErrorConnectionGroup()
                hideLoadingAnimation()
                hideLoadingTextGroup()
            }
        })

        setListeners()

        setUpRecyclerView()

        setUpAdapter()

        playLoadingAnimation()
        showLoadingTextGroup()

        launch {
            Repository.getPokemonListFromDatabase().observe(this@PokemonListActivity2, Observer {
                if (it == null) {
                    Repository.getPokemonListFromNetwork().observe(this@PokemonListActivity2, networkObserver)
                } else {
                    pokemonListAdapter.setLoaded()
                    pokemonListAdapter.updateList(it)
                    hideErrorConnectionGroup()
                    hideLoadingAnimation()
                    hideLoadingTextGroup()
                }
            })
        }
        //        hideErrorConnectionGroup();
    }

    private fun setListeners() {
        whiteBackgroundView.setOnClickListener(this)
    }

    private fun setUpAdapter() {
        pokemonListAdapter.setOnLoadMoreListener(this)
        pokemonListAdapter.setOnItemClickListener(this)
        pokemonListAdapter.setRecyclerView(recyclerView)
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(this@PokemonListActivity2, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = pokemonListAdapter
    }


    private fun showErrorConnectionGroup() {
        connectionErrorGroup.visibility = View.VISIBLE
        loadingLottieView.playAnimation()
        whiteBackgroundView.isClickable = true
        whiteBackgroundView.isFocusableInTouchMode = true
    }

    private fun hideErrorConnectionGroup() {
        whiteBackgroundView.isClickable = false
        whiteBackgroundView.isFocusableInTouchMode = false
        connectionErrorGroup.visibility = View.GONE
    }

    private fun showLoadingTextGroup() {
        loadingGroup.visibility = View.VISIBLE
    }

    private fun hideLoadingTextGroup() {
        loadingGroup.visibility = View.GONE
    }

    private fun playLoadingAnimation() {
        loadingLottieView.visibility = View.VISIBLE
        loadingLottieView.playAnimation()
    }

    private fun hideLoadingAnimation() {
        loadingLottieView.cancelAnimation()
        loadingLottieView.visibility = View.GONE
    }


    override fun onItemClick(position: Int) {
        val intent = Intent(this@PokemonListActivity2, PokemonDetailActivity2::class.java)
        val pokemonItemList = pokemonListViewModel.mutablePokemonList.value
        intent.putExtra(ApiConstants.URL, pokemonItemList!![position].url)
        intent.putExtra(ApiConstants.POKEMON_NAME, pokemonItemList[position].name)
        intent.putExtra(ApiConstants.POKEMON_ID, position + 1)
        startActivity(intent)
    }

    override fun onLoadMore() {
        pokemonListViewModel.loadMorePokemons()

    }

    override fun onClick(v: View) {
        hideErrorConnectionGroup()
        showLoadingTextGroup()
        playLoadingAnimation()
        pokemonListViewModel.loadInitialPokemonList()
    }
}
