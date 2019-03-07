package com.test.pokedex

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.pokedex.adapters.PokemonListAdapter2
import com.test.pokedex.network.ApiConstants
import com.test.pokedex.network.PokeApiService
import com.test.pokedex.network.models.pokemon_list.PokemonItem
import com.test.pokedex.view_model.PokemonListViewModel2
import kotlinx.android.synthetic.main.activity_pokemon_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class PokemonListActivity2 : AppCompatActivity(), CoroutineScope, RecyclerViewPagingAdapter.OnLoadMoreListener, View.OnClickListener, PokemonListAdapter2.PokemonClickListener {
    override fun onPokemonClick(position: Int, pokemonItem: PokemonItem) {
        val intent = Intent(this@PokemonListActivity2, PokemonDetailActivity2::class.java)
        intent.putExtra(ApiConstants.URL, pokemonItem.url)
        intent.putExtra(ApiConstants.POKEMON_NAME, pokemonItem.name)
        intent.putExtra(ApiConstants.POKEMON_ID, position + 1)
        startActivity(intent)
    }

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @Inject
    lateinit var pokeApiService: PokeApiService

    private val pokemonListAdapter2: PokemonListAdapter2 by lazy {
        PokemonListAdapter2(this)
    }

    private val pokemonListViewModel2: PokemonListViewModel2 by lazy {
        ViewModelProviders.of(this).get(PokemonListViewModel2::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pokemon_list)

        PokeApplication.appComponent()!!.injectPokemonListActivity(this)

        pokemonListViewModel2.getPokemonList().observe(this, Observer { pokemonItems ->
            pokemonListAdapter2.addData(pokemonItems) { wiewTypeToShow ->
                when (wiewTypeToShow) {
                    ViewTypeToShow.LIST_VIEW -> {
                        hideErrorConnectionGroup()
                        hideLoadingAnimation()
                        hideLoadingTextGroup()
                    }
                    ViewTypeToShow.EMPTY_VIEW -> {
                        hideLoadingAnimation()
                        hideLoadingTextGroup()
                        showErrorConnectionGroup()
                    }
                }
            }
        })

        setListeners()

        setUpRecyclerView()

        setUpAdapter()

        playLoadingAnimation()
        showLoadingTextGroup()

    }

    private fun setListeners() {
        whiteBackgroundView.setOnClickListener(this)
    }

    private fun setUpAdapter() {
        pokemonListAdapter2.setUpLoadMoreListener(recyclerView, this)
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(this@PokemonListActivity2, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = pokemonListAdapter2

//        recyclerView.itemAnimator = SlideIn()
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

    override fun onLoadMore() {
//        pokemonListViewModel.loadMorePokemons()
        pokemonListViewModel2.loadMore()
    }

    override fun onClick(v: View) {
        hideErrorConnectionGroup()
        showLoadingTextGroup()
        playLoadingAnimation()
//        pokemonListViewModel.loadInitialPokemonList()
    }
}
