package com.test.pokedex

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonSyntaxException
import com.test.pokedex.data_manager.pokemonDao
import com.test.pokedex.data_manager.pokemonListDAO
import com.test.pokedex.network.models.pokedex.pokemon2.Pokemon
import com.test.pokedex.network.models.pokemon_list.PokemonItem
import com.test.pokedex.network.pokeApiService
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.net.SocketTimeoutException

object Repository {

    val TAG = this::class.java.simpleName

    private inline fun <reified RESPONSE : DataResponse<DATA>, reified DATA : Any?>
            networkCall(callClient: Call<RESPONSE>): LiveData<Resource<DATA>> {
//        return ResourceCallHandler(callClient).makeCall()
        val data = MutableLiveData<Resource<DATA>>()

        callClient.enqueue(object : Callback<RESPONSE> {
            override fun onFailure(call: Call<RESPONSE>, t: Throwable) {
                val message = if (t is JsonSyntaxException) "JSON syntax error" else getMessageFromThrowable(t)
                Timber.e("API failure :$message")
                debugToast("API failure :$message")
                data.postValue(Resource.error(message, null))
            }

            override fun onResponse(call: Call<RESPONSE>, response: Response<RESPONSE>) {
                if (response.isSuccessful) {
                    val dataResponse = response.body() as DataResponse<DATA>
                    data.postValue(Resource.success(dataResponse.retrieveData(), ""))
                } else {
                    val message = getErrorMessage(response)
                    Timber.e("API error :$message")
                    debugToast("API error :$message")
                    data.postValue(Resource.error(message, null))
                }
            }
        })
        return data
    }

    suspend fun savePokemon(pokemon: Pokemon) {
        withContext(Dispatchers.Default) {
            pokemonDao.savePokemon(pokemon)
        }
    }

    suspend fun savePokemons(pokemonList: ArrayList<Pokemon>) {
        withContext(Dispatchers.Default) {
            pokemonDao.savePokemons(pokemonList)
        }
    }

    fun getPokemons(): LiveData<List<Pokemon>> {
        return pokemonDao.getAllPokemons()
    }

    suspend fun getPokemonByIdFromDatabase(pokemonId: Int): LiveData<Pokemon> {
        return withContext(Dispatchers.Default) {
            pokemonDao.getPokemonById(pokemonId)
        }
    }

    fun getPokemonByIdFromNetwork(pokemonId: Int): LiveData<Resource<Pokemon>> {
        return networkCall(pokeApiService.getPokemonByID(pokemonId))
    }

    fun getPokemonListFromNetwork(): LiveData<Resource<ArrayList<PokemonItem>?>> {
        return networkCall(pokeApiService.getPokemonList())
    }

    fun getPokemonListFromNetwork(limit: Int, offset: Int): LiveData<Resource<ArrayList<PokemonItem>?>> {
        return networkCall(pokeApiService.getPokemonList(limit, offset))
    }

    suspend fun getPokemonListFromDatabase(): LiveData<List<PokemonItem>> {
        return withContext(Dispatchers.Default) {
            pokemonListDAO.getPokemonList()
        }
    }

    suspend fun getPokemonListFromDatabase(limit: Int, offset: Int): LiveData<List<PokemonItem>> {
        return withContext(Dispatchers.Default) { pokemonListDAO.getPokemonList(limit, offset) }
    }

    suspend fun savePokemonItems(pokemonList: ArrayList<PokemonItem>) {
        withContext(Dispatchers.Default) {
            pokemonListDAO.savePokemonList(pokemonList)
        }
    }

}

private fun getMessageFromThrowable(t: Throwable): String {
    var message = "API failure reason unknown"
    when (t) {
        is SocketTimeoutException -> {
            message = "Slow Network, couldn't get the data"
        }
        is HttpException -> {
            t.response().errorBody()?.let {
                val json = JSONObject(it.string())
                if (json.has("message")) {
                    val errorMessage = json.getString("message")
                    if (errorMessage.isNotEmpty())
                        message = errorMessage
                }
            }
            t.message?.let {
                if (it.isNotEmpty())
                    message = it
            }
        }
    }

    return message
}

private fun <RESPONSE : Any> getErrorMessage(response: Response<RESPONSE>): String {
    var message = "API failure reason unknown"
    response.errorBody()?.let {
        val json = JSONObject(it.string())
        if (json.has("message")) {
            val errorMessage = json.getString("message")
            if (errorMessage.isNotEmpty())
                message = errorMessage
        }
    }
    /*response.message()?.let {
        if (it.isNotEmpty())
            message = it
    }*/
    return message
}



