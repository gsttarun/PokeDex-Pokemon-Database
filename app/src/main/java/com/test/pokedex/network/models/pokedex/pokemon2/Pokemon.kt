package com.test.pokedex.network.models.pokedex.pokemon2

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.test.pokedex.DataResponse

@Entity
data class Pokemon @JvmOverloads constructor(

        @Ignore
        @field:SerializedName("location_area_encounters")
        var locationAreaEncounters: String? = null,

        @Ignore
        @field:SerializedName("types")
        var types: List<TypesItem?>? = null,

        @field:SerializedName("base_experience")
        var baseExperience: Int? = null,

        @Ignore
        @field:SerializedName("held_items")
        var heldItems: List<Any?>? = null,

        @field:SerializedName("weight")
        var weight: Int? = null,

        @field:SerializedName("is_default")
        var default: Boolean? = null,

        @Embedded
        @field:SerializedName("sprites")
        var sprites: Sprites? = null,

        @field:SerializedName("abilities")
        @Embedded
        var abilities: ArrayList<AbilitiesItem?>? = null,

        @Ignore
        @field:SerializedName("game_indices")
        var gameIndices: List<GameIndicesItem?>? = null,

        @Ignore
        @field:SerializedName("species")
        var species: Species? = null,

        @Ignore
        @field:SerializedName("stats")
        var stats: List<StatsItem?>? = null,

        @Ignore
        @field:SerializedName("moves")
        var moves: List<MovesItem?>? = null,

        @field:SerializedName("name")
        var name: String? = null,

        @field:PrimaryKey
        @field:SerializedName("id")
        var id: Int? = null,

        @field:Ignore
        @field:SerializedName("forms")
        var forms: List<FormsItem?>? = null,

        @field:SerializedName("height")
        var height: Int? = null,

        @field:SerializedName("order")
        var order: Int? = null
) : DataResponse<Pokemon> {
    override fun retrieveData(): Pokemon {
        return this
    }

}