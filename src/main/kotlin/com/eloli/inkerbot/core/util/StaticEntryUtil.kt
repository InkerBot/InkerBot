package com.eloli.inkerbot.core.util

import com.eloli.inkerbot.api.plugin.PluginMeta
import com.google.inject.Injector

class StaticEntryUtil private constructor() {
    companion object {
        const val INKERBOT_ENTRY_CLASS = "com.eloli.inkerbot.api.InkerBot"
        fun applyInjector(classLoader: ClassLoader, injector: Injector) {
            try {
                val clazz = classLoader.loadClass(INKERBOT_ENTRY_CLASS)
                val injectorField = clazz.getDeclaredField("realInjector")
                injectorField.isAccessible = true
                injectorField[null] = injector
            } catch (e: RuntimeException) {
                throw e
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

    init {
        PluginMeta.builder {

        }.build()
        throw IllegalCallerException("Static class shouldn't be instance.")
    }
}