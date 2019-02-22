package com.test.pokedex.data_manager

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.pokedex.network.models.pokemon_list.PokemonItem

@Dao
interface PokemonListDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePokemonItem(pokemon: PokemonItem)

    @Query("Select * from PokemonItem")
    fun getPokemonList(): LiveData<List<PokemonItem>>

    @Query("Select * from PokemonItem limit :limit,:offset")
    fun getPokemonList(limit: Int, offset: Int): List<PokemonItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePokemonList(pokemonsList: ArrayList<PokemonItem>)

    @Query("Select * from PokemonItem where id = :pokemonId")
    fun getPokemonItemById(pokemonId: Int): LiveData<PokemonItem>

}