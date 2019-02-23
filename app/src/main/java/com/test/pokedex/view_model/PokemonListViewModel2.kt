package com.test.pokedex.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.pokedex.Repository
import com.test.pokedex.Status
import com.test.pokedex.network.models.pokemon_list.PokemonItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PokemonListViewModel2 : ViewModel() {
    private val job = Job()
    val uiCoroutineScope = CoroutineScope(Dispatchers.Main + job)

    private lateinit var pageLiveData: MutableLiveData<Int>
    private lateinit var pokemonItems: MediatorLiveData<List<PokemonItem>>
    private var currentPage = 0
    private val PAGE_ITEM_LIMIT = 20

    fun getPokemonList(): LiveData<List<PokemonItem>> {
        currentPage = 0
        pageLiveData = MutableLiveData()
        pokemonItems = MediatorLiveData()
        loadNextData()
        return pokemonItems
    }

    private fun loadNextData() {
        uiCoroutineScope.launch {
            val pokemonListFromDatabase = Repository.getPokemonListFromDatabase(PAGE_ITEM_LIMIT, currentPage * PAGE_ITEM_LIMIT)
            pokemonItems.addSource(pokemonListFromDatabase) {
                pokemonItems.removeSource(pokemonListFromDatabase)
                if (it.isEmpty()) {
                    val networkData = Repository.getPokemonListFromNetwork(PAGE_ITEM_LIMIT, currentPage * PAGE_ITEM_LIMIT)
                    pokemonItems.addSource(networkData) { responseResource ->
                        when (responseResource.status) {
                            Status.SUCCESS -> {
                                pokemonItems.removeSource(networkData)
                                if (!responseResource.data.isNullOrEmpty()) {
                                    uiCoroutineScope.launch {
                                        Repository.savePokemonItems(responseResource.data)
                                    }
                                    pokemonItems.postValue(responseResource.data)
                                }
                            }
                            Status.ERROR -> {
                                pokemonItems.removeSource(networkData)
                            }
                        }
                    }

                } else
                    pokemonItems.postValue(it)
            }
        }
    }

    internal fun loadMore() {
        currentPage++
        loadNextData()
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}
