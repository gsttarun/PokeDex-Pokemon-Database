
package com.test.pokedex.network.models.pokemon_list;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PokemonListResponse {

    @SerializedName("count")
    private Long mCount;
    @SerializedName("next")
    private String mNext;
    @SerializedName("previous")
    private Object mPrevious;
    @SerializedName("results")
    private List<Result> mResults;

    public Long getCount() {
        return mCount;
    }

    public void setCount(Long count) {
        mCount = count;
    }

    public String getNext() {
        return mNext;
    }

    public void setNext(String next) {
        mNext = next;
    }

    public Object getPrevious() {
        return mPrevious;
    }

    public void setPrevious(Object previous) {
        mPrevious = previous;
    }

    public List<Result> getResults() {
        return mResults;
    }

    public void setResults(List<Result> results) {
        mResults = results;
    }

}
