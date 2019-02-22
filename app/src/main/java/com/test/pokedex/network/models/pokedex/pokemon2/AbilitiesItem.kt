package com.test.pokedex.network.models.pokedex.pokemon2

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class AbilitiesItem(

        @field:SerializedName("is_hidden")
        var isHidden: Boolean? = null,

        @field:SerializedName("ability")
        @Embedded
        var ability: Ability? = null,

        @field:SerializedName("slot")
        var slot: Int? = null
)