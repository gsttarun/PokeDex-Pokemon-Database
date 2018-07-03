
package com.test.pokedex.network.models.pokemon;

import com.google.gson.annotations.SerializedName;

public class Sprites {

    @SerializedName("back_default")
    private String mBackDefault;
    @SerializedName("back_female")
    private Object mBackFemale;
    @SerializedName("back_shiny")
    private String mBackShiny;
    @SerializedName("back_shiny_female")
    private Object mBackShinyFemale;
    @SerializedName("front_default")
    private String mFrontDefault;
    @SerializedName("front_female")
    private Object mFrontFemale;
    @SerializedName("front_shiny")
    private String mFrontShiny;
    @SerializedName("front_shiny_female")
    private Object mFrontShinyFemale;

    public String getBackDefault() {
        return mBackDefault;
    }

    public void setBackDefault(String backDefault) {
        mBackDefault = backDefault;
    }

    public Object getBackFemale() {
        return mBackFemale;
    }

    public void setBackFemale(Object backFemale) {
        mBackFemale = backFemale;
    }

    public String getBackShiny() {
        return mBackShiny;
    }

    public void setBackShiny(String backShiny) {
        mBackShiny = backShiny;
    }

    public Object getBackShinyFemale() {
        return mBackShinyFemale;
    }

    public void setBackShinyFemale(Object backShinyFemale) {
        mBackShinyFemale = backShinyFemale;
    }

    public String getFrontDefault() {
        return mFrontDefault;
    }

    public void setFrontDefault(String frontDefault) {
        mFrontDefault = frontDefault;
    }

    public Object getFrontFemale() {
        return mFrontFemale;
    }

    public void setFrontFemale(Object frontFemale) {
        mFrontFemale = frontFemale;
    }

    public String getFrontShiny() {
        return mFrontShiny;
    }

    public void setFrontShiny(String frontShiny) {
        mFrontShiny = frontShiny;
    }

    public Object getFrontShinyFemale() {
        return mFrontShinyFemale;
    }

    public void setFrontShinyFemale(Object frontShinyFemale) {
        mFrontShinyFemale = frontShinyFemale;
    }

}
