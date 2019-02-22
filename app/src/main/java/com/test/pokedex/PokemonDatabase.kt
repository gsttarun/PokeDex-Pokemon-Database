package com.test.pokedex

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.pokedex.PokeApplication.Companion.mApplication
import com.test.pokedex.network.models.pokedex.pokemon2.Pokemon

@Database(entities = [Pokemon::class], version = 2)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDAO
}

val pokemonDatabase by lazy {
    Room.databaseBuilder(
            mApplication,
            PokemonDatabase::class.java, "pokemon_database"
    ).fallbackToDestructiveMigration().build()
}