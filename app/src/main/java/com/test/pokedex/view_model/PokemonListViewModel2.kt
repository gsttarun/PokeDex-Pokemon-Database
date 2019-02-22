package com.test.pokedex.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.pokedex.Repository
import com.test.pokedex.network.models.pokemon_list.PokemonItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PokemonListViewModel2 : ViewModel() {
    private val job = Job()
    val uiCoroutineScope = CoroutineScope(Dispatchers.Main + job)

    private lateinit var pageLiveData: MutableLiveData<Int>
    private lateinit var pokemonItems: MutableLiveData<List<PokemonItem>>
    private var currentPage = 0
    private val PAGE_ITEM_LIMIT = 20

    fun getPokemonList(albumId: String = ""): LiveData<List<PokemonItem>> {
        currentPage = 0
        pageLiveData = MutableLiveData()
        pokemonItems = MutableLiveData()
        loadNextData()
        return pokemonItems
    }

    private fun loadNextData() {
        uiCoroutineScope.launch {
            val deferred = Repository.getPokemonListFromDatabase(PAGE_ITEM_LIMIT, currentPage * PAGE_ITEM_LIMIT)
            val list = deferred.await()
            if (list.isEmpty()) {
                Repository.getPokemonListFromNetwork(PAGE_ITEM_LIMIT, currentPage * PAGE_ITEM_LIMIT)
            }
            pokemonItems.postValue(list)
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
