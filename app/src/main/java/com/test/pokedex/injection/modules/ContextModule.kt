package com.test.pokedex.injection.modules

import android.content.Context

import dagger.Module
import dagger.Provides

@Module
class ContextModule(val context: Context) {
    @Provides
    fun context(): Context {
        return context
    }
}
