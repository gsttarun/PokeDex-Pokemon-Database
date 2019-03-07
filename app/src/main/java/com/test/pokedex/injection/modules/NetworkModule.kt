package com.test.pokedex.injection.modules

import android.content.Context
import com.test.pokedex.BuildConfig
import com.test.pokedex.network.ApiConstants.POKEDEX_API
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module(includes = [ContextModule::class])
class NetworkModule {

    internal var userAgent = "User-Agent: PokemonList App (no.website.yet," + BuildConfig.VERSION_NAME + ")"

    @Provides
    @Singleton
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }

    @Provides
    @Singleton
    @Named(POKEDEX_API)
    fun providesOkHttpClient2(httpLoggingInterceptor: HttpLoggingInterceptor, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }


    @Provides
    @Singleton
    fun providesCache(@Named("cacheFile") cacheFile: File): Cache {
        return Cache(cacheFile, (50 * 10 * 1000).toLong())//50 MB
    }

    @Provides
    @Singleton
    @Named("cacheFile")
    fun providesCacheFile(context: Context): File {
        val okHttpCache = File(context.cacheDir, "okhttp_cache")
        okHttpCache.mkdirs()
        return okHttpCache
    }

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Timber.i(message) })
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }


}
