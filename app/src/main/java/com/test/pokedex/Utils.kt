package com.test.pokedex

import android.widget.Toast
import com.test.pokedex.PokeApplication.Companion.mApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun debugToast(message: String?) {
    if (BuildConfig.DEBUG) {
        Toast.makeText(mApplication, message, Toast.LENGTH_SHORT).show()
    }
}

inline fun doWithDelayOnMainThread(delayInMillis: Long, crossinline block: () -> Unit) {
    GlobalScope.launch(Dispatchers.Main) {
        delay(delayInMillis)
        block.invoke()
    }
}

