
package com.test.pokedex.network.models.pokemon;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pokemon1 {

    @SerializedName("abilities")
    private List<Ability> mAbilities;
    @SerializedName("base_experience")
    private Long mBaseExperience;
    @SerializedName("forms")
    private List<Form> mForms;
    @SerializedName("game_indices")
    private List<GameIndex> mGameIndices;
    @SerializedName("height")
    private Long mHeight;
    @SerializedName("held_items")
    private List<Object> mHeldItems;
    @SerializedName("id")
    private Long mId;
    @SerializedName("is_default")
    private Boolean mIsDefault;
    @SerializedName("location_area_encounters")
    private String mLocationAreaEncounters;
    @SerializedName("moves")
    private List<Move> mMoves;
    @SerializedName("name")
    private String mName;
    @SerializedName("order")
    private Long mOrder;
    @SerializedName("species")
    private Species mSpecies;
    @SerializedName("sprites")
    private Sprites mSprites;
    @SerializedName("stats")
    private List<Stat> mStats;
    @SerializedName("types")
    private List<Type> mTypes;
    @SerializedName("weight")
    private Long mWeight;

    public List<Ability> getAbilities() {
        return mAbilities;
    }

    public void setAbilities(List<Ability> abilities) {
        mAbilities = abilities;
    }

    public Long getBaseExperience() {
        return mBaseExperience;
    }

    public void setBaseExperience(Long baseExperience) {
        mBaseExperience = baseExperience;
    }

    public List<Form> getForms() {
        return mForms;
    }

    public void setForms(List<Form> forms) {
        mForms = forms;
    }

    public List<GameIndex> getGameIndices() {
        return mGameIndices;
    }

    public void setGameIndices(List<GameIndex> gameIndices) {
        mGameIndices = gameIndices;
    }

    public Long getHeight() {
        return mHeight;
    }

    public void setHeight(Long height) {
        mHeight = height;
    }

    public List<Object> getHeldItems() {
        return mHeldItems;
    }

    public void setHeldItems(List<Object> heldItems) {
        mHeldItems = heldItems;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Boolean getIsDefault() {
        return mIsDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        mIsDefault = isDefault;
    }

    public String getLocationAreaEncounters() {
        return mLocationAreaEncounters;
    }

    public void setLocationAreaEncounters(String locationAreaEncounters) {
        mLocationAreaEncounters = locationAreaEncounters;
    }

    public List<Move> getMoves() {
        return mMoves;
    }

    public void setMoves(List<Move> moves) {
        mMoves = moves;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Long getOrder() {
        return mOrder;
    }

    public void setOrder(Long order) {
        mOrder = order;
    }

    public Species getSpecies() {
        return mSpecies;
    }

    public void setSpecies(Species species) {
        mSpecies = species;
    }

    public Sprites getSprites() {
        return mSprites;
    }

    public void setSprites(Sprites sprites) {
        mSprites = sprites;
    }

    public List<Stat> getStats() {
        return mStats;
    }

    public void setStats(List<Stat> stats) {
        mStats = stats;
    }

    public List<Type> getTypes() {
        return mTypes;
    }

    public void setTypes(List<Type> types) {
        mTypes = types;
    }

    public Long getWeight() {
        return mWeight;
    }

    public void setWeight(Long weight) {
        mWeight = weight;
    }

}
