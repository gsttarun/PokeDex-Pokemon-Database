package com.test.pokedex.injection.modules

import android.content.Context
import com.squareup.picasso.Cache
import com.squareup.picasso.LruCache
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, ContextModule::class])
class ImageLoaderModule {

    @Provides
    @Singleton
    fun providesPicasso(context: Context, cache: Cache, okHttp3Downloader: OkHttp3Downloader): Picasso {
        return Picasso.Builder(context)
                .downloader(okHttp3Downloader)
                .memoryCache(cache)
                .build()
    }

    @Provides
    @Singleton
    fun providesCache(context: Context): Cache {
        return LruCache(context)
    }

    @Provides
    @Singleton
    fun providesOkHttp3Downloader(okHttpClient: OkHttpClient): OkHttp3Downloader {
        return OkHttp3Downloader(okHttpClient)
    }

}
