package com.eloli.inkerbot.core.event

import com.eloli.inkerbot.api.event.EventContextKey
import com.eloli.inkerbot.api.util.ResourceKey
import java.lang.reflect.Type
import java.util.*

class InkEventContextKey<T>(override val key: ResourceKey, private val type: Class<T>) : EventContextKey<T> {
    override fun allowedType(): Type {
        return type
    }

    override fun isInstance(value: Any): Boolean {
        return type.isInstance(value)
    }

    override fun cast(value: Any): T {
        return type.cast(value)
    }

    class Builder<T>:EventContextKey.Builder<T>{
        private var key: ResourceKey? = null;
        private var type: Class<T>? = null;

        override fun build(): EventContextKey<T> {
            Objects.requireNonNull(key,"key")
            Objects.requireNonNull(type,"type")
            return InkEventContextKey(key!!, type!!)
        }

        override fun key(key: ResourceKey): EventContextKey.Builder<T> {
            Objects.requireNonNull(key,"key")
            this.key = key
            return this
        }

        override fun <N> type(type: Class<N>): EventContextKey.Builder<N> {
            Objects.requireNonNull(type,"type")
            val builder:Builder<N> = Builder()
            builder.key = key
            builder.type = type
            return builder
        }
    }
}