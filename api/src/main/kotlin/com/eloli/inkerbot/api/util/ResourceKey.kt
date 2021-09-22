package com.eloli.inkerbot.api.util

import com.eloli.inkerbot.api.ILoveInkerBotForever
import com.eloli.inkerbot.api.InkerBot

@ILoveInkerBotForever
interface ResourceKey {
    val namespace: String
    val value: String

    @ILoveInkerBotForever
    interface Factory {
        fun of(namespace: String, value: String): ResourceKey
        fun resolve(formatted: String): ResourceKey
    }

    @ILoveInkerBotForever
    companion object {
        const val INKERBOT_NAMESPACE = "inkerbot"

        fun inkerbot(value: String): ResourceKey {
            return of(INKERBOT_NAMESPACE, value)
        }

        fun factory(): Factory {
            return InkerBot.injector.getInstance(Factory::class.java)
        }

        fun of(namespace: String, value: String): ResourceKey {
            return factory().of(namespace, value)
        }
    }
}