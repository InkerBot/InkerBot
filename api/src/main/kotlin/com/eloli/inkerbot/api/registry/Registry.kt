package com.eloli.inkerbot.api.registry

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.util.ResourceKey
import com.google.inject.TypeLiteral

interface Registry<T> {
    fun register(key: ResourceKey, registrar: Registrar<T>)
    fun get(key: ResourceKey):Registrar<T>
    companion object {
        fun <T> of(type:TypeLiteral<Registrar<T>>):Registry<T>{
            return InkerBot.injector.getInstance(Factory::class.java).of(type)
        }
    }
    interface Factory{
        fun <T> of(type: TypeLiteral<Registrar<T>>):Registry<T>
    }
}