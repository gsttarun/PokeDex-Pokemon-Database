package com.test.pokedex.network.models.pokemon_list

import com.google.gson.annotations.SerializedName
import com.test.pokedex.DataResponse

data class PokemonListResponse(
        @SerializedName("count")
        var count: Long? = null,

        @SerializedName("next")
        var next: String? = null,

        @SerializedName("previous")
        var previous: Any? = null,

        @SerializedName("results")
        var pokemonItems: ArrayList<PokemonItem?>? = null
) : DataResponse<ArrayList<PokemonItem?>?> {
    override fun retrieveData(): ArrayList<PokemonItem?>? {
        return pokemonItems
    }
}
