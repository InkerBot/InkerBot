package com.eloli.inkerbot.core.registry

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.plugin.PluginManager
import com.eloli.inkerbot.api.registry.Registrar
import com.eloli.inkerbot.api.registry.Registry
import com.eloli.inkerbot.api.util.ResourceKey
import com.eloli.inkerbot.core.InkerBotPluginContainer
import com.eloli.inkerbot.core.plugin.JvmPluginContainer
import com.google.inject.ConfigurationException
import com.google.inject.Key
import com.google.inject.TypeLiteral
import com.google.inject.name.Names
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InkRegistry<T>(val type: TypeLiteral<Registrar<T>>) : Registry<T> {
  @Inject
  private lateinit var pluginManager: PluginManager
  private val map: MutableMap<ResourceKey, Registrar<T>> = HashMap()
  override fun register(key: ResourceKey, registrar: Registrar<T>) {
    if (map.contains(key)) {
      throw IllegalStateException("ResourceKey $key have been register with ${map[key]}.")
    }
    map[key] = registrar
  }

  override fun get(key: ResourceKey): Registrar<T> {
    return map.getOrPut(key) {
      pluginManager.plugins.forEach {
        val injector = when (it) {
          is JvmPluginContainer -> {
            it.injector
          }
          is InkerBotPluginContainer -> {
            InkerBot.injector
          }
          else -> {
            return@forEach
          }
        }
        try {
          return@getOrPut InkerBot.serviceManager.getInstance(
            Key.get(type).withAnnotation(Names.named(key.toString()))
          )
        } catch (e: ConfigurationException) {
          //
        }
      }
      throw IllegalStateException("Could not found resourceKey $key.")
    }
  }

  @Singleton
  class Factory : Registry.Factory {
    private val map: MutableMap<TypeLiteral<Registrar<*>>, Registry<*>> = HashMap()
    override fun <T> of(type: TypeLiteral<Registrar<T>>): Registry<T> {
      return map.getOrPut(type as TypeLiteral<Registrar<*>>) {
        InkRegistry(type as TypeLiteral<Registrar<T>>).apply {
          InkerBot.injector.injectMembers(this)
        }
      } as Registry<T>
    }
  }
}