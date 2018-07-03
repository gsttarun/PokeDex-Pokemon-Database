package com.test.pokedex.injection.modules;

import android.content.Context;

import com.squareup.picasso.Cache;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module(includes = {NetworkModule.class, ContextModule.class})
public class ImageLoaderModule {

    @Provides
    @Singleton
    public Picasso providesPicasso(Context context, Cache cache, OkHttp3Downloader okHttp3Downloader) {
        return new Picasso.Builder(context)
                .downloader(okHttp3Downloader)
                .memoryCache(cache)
                .build();
    }

    @Provides
    @Singleton
    public Cache providesCache(Context context) {
        return new LruCache(context);
    }

    @Provides
    @Singleton
    public OkHttp3Downloader providesOkHttp3Downloader(OkHttpClient okHttpClient) {
        return new OkHttp3Downloader(okHttpClient);
    }

}
