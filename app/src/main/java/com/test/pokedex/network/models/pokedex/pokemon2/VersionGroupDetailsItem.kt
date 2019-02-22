package com.test.pokedex.network.models.pokedex.pokemon2

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class VersionGroupDetailsItem(

	@field:SerializedName("level_learned_at")
	val levelLearnedAt: Int? = null,

	@field:SerializedName("version_group")
	val versionGroup: VersionGroup? = null,

	@field:SerializedName("move_learn_method")
	val moveLearnMethod: MoveLearnMethod? = null
)