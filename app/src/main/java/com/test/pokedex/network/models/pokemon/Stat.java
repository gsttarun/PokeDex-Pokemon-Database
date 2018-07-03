
package com.test.pokedex.network.models.pokemon;

import com.google.gson.annotations.SerializedName;

public class Stat {

    @SerializedName("base_stat")
    private Long mBaseStat;
    @SerializedName("effort")
    private Long mEffort;
    @SerializedName("name")
    private String mName;
    @SerializedName("stat")
    private Stat mStat;
    @SerializedName("url")
    private String mUrl;

    public Long getBaseStat() {
        return mBaseStat;
    }

    public void setBaseStat(Long baseStat) {
        mBaseStat = baseStat;
    }

    public Long getEffort() {
        return mEffort;
    }

    public void setEffort(Long effort) {
        mEffort = effort;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Stat getStat() {
        return mStat;
    }

    public void setStat(Stat stat) {
        mStat = stat;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

}
