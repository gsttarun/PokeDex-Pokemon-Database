package com.test.pokedex.network.models.pokedex.pokemon2

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class Ability(

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("url")
	var url: String? = null
)