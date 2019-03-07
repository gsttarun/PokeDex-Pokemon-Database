package com.test.pokedex;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

public class TimberLogImplementation {

    public static void init() {
        Timber.plant(new ReleaseTree());
    }
}