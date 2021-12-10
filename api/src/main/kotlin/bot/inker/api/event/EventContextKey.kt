package bot.inker.api.event

import bot.inker.api.builder.AbstractBuilder
import bot.inker.api.plugin.PluginMeta
import bot.inker.api.util.ResourceKey
import bot.inker.api.util.ResourceKeyed
import java.lang.reflect.Type

interface EventContextKey<T> : ResourceKeyed {
  fun allowedType(): Type
  fun isInstance(value: Any): Boolean
  fun cast(value: Any): T

  interface Builder<T> : AbstractBuilder<EventContextKey<T>> {
    fun key(key: ResourceKey): Builder<T>
    fun <N> type(type: Class<N>): Builder<N>
  }

  companion object {
    fun builder(builder: PluginMeta.Builder.() -> Unit): PluginMeta.Builder {
      return bot.inker.api.InkerBot.injector.getInstance(PluginMeta.Builder::class.java).apply(builder)
    }

    fun builder(): Builder<*> {
      return bot.inker.api.InkerBot.injector.getInstance(Builder::class.java)
    }

    fun <T> of(key: ResourceKey, type: Class<T>): EventContextKey<T> {
      return builder().key(key).type(type).build()
    }
  }
}