package com.eloli.inkerbot.api.util

import com.eloli.inkerbot.core.inkerbot.api.InkerBot

interface ResourceKey {
    val namespace:String
    val value:String

    interface Factory {
        fun of(namespace: String, value: String): ResourceKey
        fun resolve(formatted: String): ResourceKey
    }

    companion object {
        const val INKERBOT_NAMESPACE = "inkerbot"

        fun inkerbot(value: String): ResourceKey {
            return of(INKERBOT_NAMESPACE, value)
        }

        fun factory(): Factory {
            return InkerBot.getInjector().getInstance(Factory::class.java)
        }

        fun of(namespace: String, value: String): ResourceKey {
            return factory().of(namespace, value)
        }
    }
}