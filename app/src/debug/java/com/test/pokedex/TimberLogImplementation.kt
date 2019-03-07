package com.test.pokedex

import timber.log.Timber

object TimberLogImplementation {
    fun init() {
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                return String.format("C:%s:%s",
                        super.createStackElementTag(element),
                        element.lineNumber)
            }
        })
    }
}