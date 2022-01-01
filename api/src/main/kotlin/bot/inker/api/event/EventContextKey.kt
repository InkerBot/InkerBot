package bot.inker.api.event

import bot.inker.api.InkerBot
import bot.inker.api.builder.AbstractBuilder
import bot.inker.api.plugin.PluginMeta
import bot.inker.api.util.ResourceKey
import bot.inker.api.util.ResourceKeyed
import java.lang.reflect.Type

interface EventContextKey<T:Any> : ResourceKeyed {
  fun fill(value:T):EventContextEntry<T>

  interface Factory{
    fun <T:Any> of(key:ResourceKey):EventContextKey<T>
  }

  companion object{
    fun <T:Any> of(key:ResourceKey):EventContextKey<T> {
      return InkerBot(Factory::class).of(key)
    }
  }
}