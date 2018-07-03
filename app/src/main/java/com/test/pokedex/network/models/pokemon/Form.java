
package com.test.pokedex.network.models.pokemon;

import com.google.gson.annotations.SerializedName;

public class Form {

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
