package com.test.pokedex.data_manager

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.pokedex.network.models.pokedex.pokemon2.Pokemon

@Dao
interface PokemonDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePokemon(pokemon: Pokemon)

    @Query("Select * from Pokemon")
    fun getAllPokemons(): LiveData<List<Pokemon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePokemons(pokemonsList: List<Pokemon>)

    @Query("Select * from Pokemon where id = :pokemonId")
    fun getPokemonById(pokemonId: Int): LiveData<Pokemon>

}