package com.test.pokedex.injection.modules;

import android.content.Context;

import com.test.pokedex.BuildConfig;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

import static com.test.pokedex.network.ApiConstants.POKEDEX_API;


@Module(includes = ContextModule.class)
public class NetworkModule {

    String userAgent = "User-Agent: PokemonList App (no.website.yet," + BuildConfig.VERSION_NAME + ")";

    @Provides
    @Singleton
    public OkHttpClient providesOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor, Cache cache) {
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    @Named(POKEDEX_API)
    public OkHttpClient providesOkHttpClient2(HttpLoggingInterceptor httpLoggingInterceptor, Cache cache) {
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }


    @Provides
    @Singleton
    public Cache providesCache(@Named("cacheFile") File cacheFile) {
        return new Cache(cacheFile, 50 * 10 * 1000);//50 MB
    }

    @Provides
    @Singleton
    public @Named("cacheFile")
    File providesCacheFile(Context context) {
        File okHttpCache = new File(context.getCacheDir(), "okhttp_cache");
        okHttpCache.mkdirs();
        return okHttpCache;
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor providesHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.i(message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }


}
