
package com.test.pokedex.network.models.pokedex.pokemon2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pokemon2 {


    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("species")
    @Expose
    private String species;
    @SerializedName("types")
    @Expose
    private List<String> types = null;
    @SerializedName("abilities")
    @Expose
    private Abilities abilities;
    @SerializedName("eggGroups")
    @Expose
    private List<String> eggGroups = null;
    @SerializedName("gender")
    @Expose
    private List<Object> gender = null;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("family")
    @Expose
    private Family family;
    @SerializedName("starter")
    @Expose
    private Boolean starter;
    @SerializedName("legendary")
    @Expose
    private Boolean legendary;
    @SerializedName("mythical")
    @Expose
    private Boolean mythical;
    @SerializedName("ultraBeast")
    @Expose
    private Boolean ultraBeast;
    @SerializedName("mega")
    @Expose
    private Boolean mega;
    @SerializedName("gen")
    @Expose
    private Integer gen;
    @SerializedName("sprite")
    @Expose
    private String sprite;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Abilities getAbilities() {
        return abilities;
    }

    public void setAbilities(Abilities abilities) {
        this.abilities = abilities;
    }

    public List<String> getEggGroups() {
        return eggGroups;
    }

    public void setEggGroups(List<String> eggGroups) {
        this.eggGroups = eggGroups;
    }

    public List<Object> getGender() {
        return gender;
    }

    public void setGender(List<Object> gender) {
        this.gender = gender;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Boolean getStarter() {
        return starter;
    }

    public void setStarter(Boolean starter) {
        this.starter = starter;
    }

    public Boolean getLegendary() {
        return legendary;
    }

    public void setLegendary(Boolean legendary) {
        this.legendary = legendary;
    }

    public Boolean getMythical() {
        return mythical;
    }

    public void setMythical(Boolean mythical) {
        this.mythical = mythical;
    }

    public Boolean getUltraBeast() {
        return ultraBeast;
    }

    public void setUltraBeast(Boolean ultraBeast) {
        this.ultraBeast = ultraBeast;
    }

    public Boolean getMega() {
        return mega;
    }

    public void setMega(Boolean mega) {
        this.mega = mega;
    }

    public Integer getGen() {
        return gen;
    }

    public void setGen(Integer gen) {
        this.gen = gen;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

}

class Family {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("evolutionStage")
    @Expose
    private Integer evolutionStage;
    @SerializedName("evolutionLine")
    @Expose
    private List<String> evolutionLine = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEvolutionStage() {
        return evolutionStage;
    }

    public void setEvolutionStage(Integer evolutionStage) {
        this.evolutionStage = evolutionStage;
    }

    public List<String> getEvolutionLine() {
        return evolutionLine;
    }

    public void setEvolutionLine(List<String> evolutionLine) {
        this.evolutionLine = evolutionLine;
    }

}

class Abilities {

    @SerializedName("normal")
    @Expose
    private List<String> normal = null;
    @SerializedName("hidden")
    @Expose
    private List<Object> hidden = null;

    public List<String> getNormal() {
        return normal;
    }

    public void setNormal(List<String> normal) {
        this.normal = normal;
    }

    public List<Object> getHidden() {
        return hidden;
    }

    public void setHidden(List<Object> hidden) {
        this.hidden = hidden;
    }

}