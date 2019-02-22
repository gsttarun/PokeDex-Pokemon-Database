package com.test.pokedex.data_manager

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.pokedex.PokeApplication.Companion.mApplication
import com.test.pokedex.network.models.pokedex.pokemon2.Pokemon
import com.test.pokedex.network.models.pokemon_list.PokemonItem

@Database(entities = [Pokemon::class, PokemonItem::class], version = 3)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDAO
    abstract fun pokemonListDao(): PokemonListDAO
}

val pokemonDatabase by lazy {
    Room.databaseBuilder(
            mApplication,
            PokemonDatabase::class.java, "pokemon_database"
    ).fallbackToDestructiveMigration().build()
}

val pokemonDao: PokemonDAO by lazy {
    pokemonDatabase.pokemonDao()
}

val pokemonListDAO: PokemonListDAO by lazy {
    pokemonDatabase.pokemonListDao()
}