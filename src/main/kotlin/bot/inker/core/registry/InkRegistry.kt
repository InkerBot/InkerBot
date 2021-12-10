package bot.inker.core.registry

import bot.inker.api.plugin.PluginManager
import bot.inker.api.registry.Registrar
import bot.inker.api.registry.Registry
import bot.inker.api.util.ResourceKey
import bot.inker.core.InkerBotPluginContainer
import bot.inker.core.plugin.JvmPluginContainer
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
            bot.inker.api.InkerBot.injector
          }
          else -> {
            return@forEach
          }
        }
        try {
          return@getOrPut bot.inker.api.InkerBot.serviceManager.getInstance(
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
          bot.inker.api.InkerBot.injector.injectMembers(this)
        }
      } as Registry<T>
    }
  }
}