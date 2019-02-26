package com.test.pokedex

import com.test.pokedex.network.models.pokemon_list.PokemonItem

import androidx.recyclerview.widget.DiffUtil

class MyDiffUtilCallBack(private val oldPokemonItems: List<PokemonItem>?, private val newPokemonItems: List<PokemonItem>?) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldPokemonItems?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newPokemonItems?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldPokemonItems!![oldItemPosition]
        val newItem = newPokemonItems!![newItemPosition]
        var matches = false

        if (oldItem != null && newItem != null) {
            matches = oldItem.name.matches(newItem.name.toRegex())
            //            Timber.v("areItemsTheSame : "+matches);
        }
        return matches
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        Timber.v("areContentsTheSame : "+matches);
        return oldPokemonItems!![oldItemPosition] === newPokemonItems!![newItemPosition]
    }
}
