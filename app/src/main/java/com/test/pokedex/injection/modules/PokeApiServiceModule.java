package com.test.pokedex.injection.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.pokedex.BuildConfig;
import com.test.pokedex.network.PokeApiService;
import com.test.pokedex.network.PokedexApiService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.test.pokedex.network.ApiConstants.POKEDEX_API;
import static com.test.pokedex.network.ApiConstants.POKE_API;


@Module ( includes = {NetworkModule.class})
public class PokeApiServiceModule {

    @Provides
    @Singleton
    public PokeApiService providePokeApiService(@Named(POKE_API) Retrofit retrofit){
        return retrofit.create(PokeApiService.class);
    }

    @Provides
    @Singleton
    public PokedexApiService providePokedexApiService(@Named(POKEDEX_API) Retrofit retrofit){
        return retrofit.create(PokedexApiService.class);
    }

    @Provides
    @Singleton
    @Named(POKE_API)
    public  Retrofit providePokeApiRetrofit(OkHttpClient okHttpClient,GsonConverterFactory gsonConverterFactory){
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.POKE_API_BASE)
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @Named(POKEDEX_API)
    public  Retrofit providePokedexApiRetrofit(@Named(POKEDEX_API) OkHttpClient okHttpClient,GsonConverterFactory gsonConverterFactory){
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.POKEDEX_API_BASE)
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public GsonConverterFactory providesGsonConverterFactory(Gson gson){
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    public Gson providesGson(){
        return new GsonBuilder().create();
    }
}
