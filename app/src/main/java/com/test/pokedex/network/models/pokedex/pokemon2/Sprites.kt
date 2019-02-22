package com.test.pokedex.network.models.pokedex.pokemon2

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class Sprites(

	@field:SerializedName("back_shiny_female")
	var backShinyFemale: String? = null,

	@field:SerializedName("back_female")
	var backFemale: String? = null,

	@field:SerializedName("back_default")
	var backDefault: String? = null,

	@field:SerializedName("front_shiny_female")
	var frontShinyFemale: String? = null,

	@field:SerializedName("front_default")
	var frontDefault: String? = null,

	@field:SerializedName("front_female")
	var frontFemale: String? = null,

	@field:SerializedName("back_shiny")
	var backShiny: String? = null,

	@field:SerializedName("front_shiny")
	var frontShiny: String? = null
)