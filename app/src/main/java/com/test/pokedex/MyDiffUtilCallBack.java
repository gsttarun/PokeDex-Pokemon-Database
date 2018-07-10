package com.test.pokedex;

import android.support.v7.util.DiffUtil;

import com.test.pokedex.network.models.pokemon_list.Result;

import java.util.List;

import timber.log.Timber;

public class MyDiffUtilCallBack extends DiffUtil.Callback {

    private List<Result> oldResults;
    private List<Result> newResults;

    public MyDiffUtilCallBack(List<Result> oldResults, List<Result> newResults) {
        this.oldResults = oldResults;
        this.newResults = newResults;
    }

    @Override
    public int getOldListSize() {
        return oldResults != null ? oldResults.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newResults != null ? newResults.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Result oldItem = oldResults.get(oldItemPosition);
        Result newItem = newResults.get(newItemPosition);
        boolean matches = oldItem.getName().matches(newItem.getName());
        Timber.v("areItemsTheSame : "+matches);
        return matches;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        boolean matches = oldResults.get(oldItemPosition).getUrl().matches(newResults.get(newItemPosition).getUrl());
        Timber.v("areContentsTheSame : "+matches);
        return matches;
    }
}
