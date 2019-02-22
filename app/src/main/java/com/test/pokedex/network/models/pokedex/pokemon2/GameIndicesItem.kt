package com.test.pokedex.network.models.pokedex.pokemon2

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class GameIndicesItem(

	@field:SerializedName("game_index")
	val gameIndex: Int? = null,

	@field:SerializedName("version")
	val version: Version? = null
)