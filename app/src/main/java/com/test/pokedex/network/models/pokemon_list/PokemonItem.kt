package com.test.pokedex.network.models.pokemon_list

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class PokemonItem(

        @PrimaryKey(autoGenerate = true)
        var id: Int,

        @SerializedName("name")
        var name: String,

        @SerializedName("url")
        var url: String
)
