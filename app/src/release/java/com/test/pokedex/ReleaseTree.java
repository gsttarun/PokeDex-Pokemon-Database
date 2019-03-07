package com.test.pokedex;

import timber.log.Timber;

import static android.util.Log.ERROR;
import static android.util.Log.WARN;

public class ReleaseTree extends Timber.Tree

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {

            if (priority == ERROR || priority == WARN) {
//      YourCrashLibrary.log(priority, tag, message);
            }
        }