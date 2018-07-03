package com.test.pokedex;

import android.app.Activity;
import android.app.Application;

import com.test.pokedex.injection.components.AppComponent;
import com.test.pokedex.injection.components.DaggerAppComponent;
import com.test.pokedex.injection.modules.ContextModule;

import timber.log.Timber;

public class PokeApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        appComponent= DaggerAppComponent
                        .builder()
                        .contextModule(new ContextModule(this))
                        .build();
    }

    public AppComponent appComponent() {
        return appComponent;
    }

    public static PokeApplication get(Activity activity) {
        return (PokeApplication) activity.getApplication();
    }
}
