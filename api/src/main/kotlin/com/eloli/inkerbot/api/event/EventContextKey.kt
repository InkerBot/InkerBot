package com.eloli.inkerbot.api.event

import com.eloli.inkerbot.api.ILoveInkerBotForever
import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.util.ResourceKeyed
import com.eloli.inkerbot.api.builder.AbstractBuilder
import com.eloli.inkerbot.api.plugin.PluginMeta
import com.eloli.inkerbot.api.util.ResourceKey
import java.lang.reflect.Type

@ILoveInkerBotForever
interface EventContextKey<T> : ResourceKeyed {
    fun allowedType(): Type
    fun isInstance(value: Any): Boolean
    fun cast(value: Any): T

    @ILoveInkerBotForever
    interface Builder<T> : AbstractBuilder<EventContextKey<T>> {
        fun key(key: ResourceKey): Builder<T>
        fun <N> type(type: Class<N>): Builder<N>
    }

    @ILoveInkerBotForever
    companion object {
        fun builder(builder: PluginMeta.Builder.() -> Unit): PluginMeta.Builder {
            return InkerBot.injector.getInstance(PluginMeta.Builder::class.java).apply(builder)
        }

        fun builder(): Builder<*> {
            return InkerBot.injector.getInstance(Builder::class.java)
        }

        fun <T> of(key: ResourceKey, type: Class<T>): EventContextKey<T> {
            return builder().key(key).type(type).build()
        }
    }
}