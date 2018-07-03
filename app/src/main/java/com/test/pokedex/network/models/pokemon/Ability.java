
package com.test.pokedex.network.models.pokemon;

import com.google.gson.annotations.SerializedName;

public class Ability {

    @SerializedName("ability")
    private Ability mAbility;
    @SerializedName("is_hidden")
    private Boolean mIsHidden;
    @SerializedName("name")
    private String mName;
    @SerializedName("slot")
    private Long mSlot;
    @SerializedName("url")
    private String mUrl;

    public Ability getAbility() {
        return mAbility;
    }

    public void setAbility(Ability ability) {
        mAbility = ability;
    }

    public Boolean getIsHidden() {
        return mIsHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        mIsHidden = isHidden;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Long getSlot() {
        return mSlot;
    }

    public void setSlot(Long slot) {
        mSlot = slot;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

}
