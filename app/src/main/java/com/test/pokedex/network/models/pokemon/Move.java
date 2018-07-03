
package com.test.pokedex.network.models.pokemon;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Move {

    @SerializedName("move")
    private Move mMove;
    @SerializedName("name")
    private String mName;
    @SerializedName("url")
    private String mUrl;
    @SerializedName("version_group_details")
    private List<VersionGroupDetail> mVersionGroupDetails;

    public Move getMove() {
        return mMove;
    }

    public void setMove(Move move) {
        mMove = move;
    }

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

    public List<VersionGroupDetail> getVersionGroupDetails() {
        return mVersionGroupDetails;
    }

    public void setVersionGroupDetails(List<VersionGroupDetail> versionGroupDetails) {
        mVersionGroupDetails = versionGroupDetails;
    }

}
