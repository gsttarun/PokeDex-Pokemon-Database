package com.test.pokedex;

import android.app.Activity;
import android.app.Application;

import com.singhajit.sherlock.core.Sherlock;
import com.test.pokedex.injection.components.AppComponent;
import com.test.pokedex.injection.components.DaggerAppComponent;
import com.test.pokedex.injection.modules.ContextModule;

public class PokeApplication extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        TimberLogImplementation.init();
        Sherlock.init(this); //Initializing Sherlock
        appComponent = DaggerAppComponent
                .builder()
                .contextModule(new ContextModule(this))
                .build();
    }

    public static AppComponent appComponent() {
        return appComponent;
    }

    public static PokeApplication get(Activity activity) {
        return (PokeApplication) activity.getApplication();
    }
}
