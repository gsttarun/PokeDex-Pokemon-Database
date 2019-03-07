package com.test.pokedex.injection.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.test.pokedex.BuildConfig
import com.test.pokedex.network.ApiConstants.POKEDEX_API
import com.test.pokedex.network.ApiConstants.POKE_API
import com.test.pokedex.network.PokeApiService
import com.test.pokedex.network.PokedexApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module(includes = [NetworkModule::class])
class PokeApiServiceModule {

    @Provides
    @Singleton
    fun providePokeApiService(@Named(POKE_API) retrofit: Retrofit): PokeApiService {
        return retrofit.create(PokeApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePokedexApiService(@Named(POKEDEX_API) retrofit: Retrofit): PokedexApiService {
        return retrofit.create(PokedexApiService::class.java)
    }

    @Provides
    @Singleton
    @Named(POKE_API)
    fun providePokeApiRetrofit(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.POKE_API_BASE)
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    @Named(POKEDEX_API)
    fun providePokedexApiRetrofit(@Named(POKEDEX_API) okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.POKEDEX_API_BASE)
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun providesGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return GsonBuilder().create()
    }
}
