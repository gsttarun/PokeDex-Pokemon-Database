package com.test.pokedex;

import com.test.pokedex.network.models.pokemon_list.PokemonItem;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

public class MyDiffUtilCallBack extends DiffUtil.Callback {

    private List<PokemonItem> oldPokemonItems;
    private List<PokemonItem> newPokemonItems;

    public MyDiffUtilCallBack(List<PokemonItem> oldPokemonItems, List<PokemonItem> newPokemonItems) {
        this.oldPokemonItems = oldPokemonItems;
        this.newPokemonItems = newPokemonItems;
    }

    @Override
    public int getOldListSize() {
        return oldPokemonItems != null ? oldPokemonItems.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newPokemonItems != null ? newPokemonItems.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        PokemonItem oldItem = oldPokemonItems.get(oldItemPosition);
        PokemonItem newItem = newPokemonItems.get(newItemPosition);
        boolean matches = false;

        if (oldItem!=null && newItem!=null) {
            matches = oldItem.getName().matches(newItem.getName());
//            Timber.v("areItemsTheSame : "+matches);
        }
        return matches;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        boolean matches = oldPokemonItems.get(oldItemPosition)== newPokemonItems.get(newItemPosition);
//        Timber.v("areContentsTheSame : "+matches);
        return matches;
    }
}
