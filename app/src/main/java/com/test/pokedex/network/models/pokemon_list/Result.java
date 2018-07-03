
package com.test.pokedex.network.models.pokemon_list;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("name")
    private String mName;
    @SerializedName("url")
    private String mUrl;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

}
