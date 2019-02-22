package com.test.pokedex

import android.app.Activity
import android.app.Application
import com.singhajit.sherlock.core.Sherlock
import com.test.pokedex.injection.components.AppComponent
import com.test.pokedex.injection.components.DaggerAppComponent
import com.test.pokedex.injection.modules.ContextModule

class PokeApplication : Application() {

    companion object {
        lateinit var mApplication: PokeApplication

        private var appComponent: AppComponent? = null

        fun appComponent(): AppComponent? {
            return appComponent
        }

        operator fun get(activity: Activity): PokeApplication {
            return activity.application as PokeApplication
        }
    }

    override fun onCreate() {
        super.onCreate()

        mApplication = this
        TimberLogImplementation.init()
        Sherlock.init(this) //Initializing Sherlock
        appComponent = DaggerAppComponent
                .builder()
                .contextModule(ContextModule(this))
                .build()
    }
}
