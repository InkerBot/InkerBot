package com.eloli.inkerbot.api.plugin

import com.eloli.inkerbot.core.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.builder.AbstractBuilder


interface PluginDepend {
    val name: String
    val type: Type

    enum class Type {
        REQUIRE, SOFT, COOPERATE, LIBRARY
    }

    interface Builder : AbstractBuilder<PluginDepend?> {
        fun name(name: String): Builder
        fun type(type: Type): Builder
    }

    companion object {
        fun of(type: Type, name: String): PluginDepend? {
            return builder().type(type).name(name).build()
        }

        fun builder(): Builder {
            return InkerBot.getInjector().getInstance(Builder::class.java)
        }
    }
}