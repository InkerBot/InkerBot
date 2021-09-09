package com.eloli.inkerbot.api.config

import com.eloli.inkerbot.api.InkerBot.Companion.injector
import com.eloli.inkerbot.api.plugin.PluginContainer

interface ConfigService<T> {
    @Throws(Exception::class)
    fun load(): T

    @Throws(Exception::class)
    fun save(obj: T)
    interface Factory {
        fun <T> of(pluginContainer: PluginContainer, name: String, configClass: Class<T>): ConfigService<T>
    }

    companion object {
        fun factory(): Factory {
            return injector.getInstance(Factory::class.java)
        }
    }
}