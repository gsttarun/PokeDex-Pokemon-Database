package com.test.pokedex.data_manager

import androidx.lifecycle.LiveData
import androidx.room.*
import com.test.pokedex.network.models.pokemon_list.PokemonItem

@Dao
interface PokemonListDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePokemonItem(pokemon: PokemonItem)

    @Query("Select * from PokemonItem")
    fun getPokemonList(): LiveData<List<PokemonItem>>

    @Query("Select * from PokemonItem limit :limit offset :offset")
    fun getPokemonList(limit: Int, offset: Int): LiveData<List<PokemonItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePokemonList(pokemonsList: ArrayList<PokemonItem?>?)

    @Query("Select * from PokemonItem where id = :pokemonId")
    fun getPokemonItemById(pokemonId: Int): LiveData<PokemonItem>

    @Query("update PokemonItem set id=:id where name=:pokemonName")
    fun addIdToPokemon(pokemonName: String, id: Int)

    @Query("Delete from PokemonItem where name = :pokemonName")
    fun deletePokemonItem(pokemonName: String): Int

    @Delete
    fun deletePokemonItem(pokemonItem: PokemonItem): Int
}