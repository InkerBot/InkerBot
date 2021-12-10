package bot.inker.api.registry

import bot.inker.api.util.ResourceKey
import com.google.inject.TypeLiteral

interface Registry<T> {
  fun register(key: ResourceKey, registrar: Registrar<T>)
  fun get(key: ResourceKey): Registrar<T>

  companion object {
    fun <T> of(type: TypeLiteral<Registrar<T>>): Registry<T> {
      return bot.inker.api.InkerBot.injector.getInstance(Factory::class.java).of(type)
    }
  }

  interface Factory {
    fun <T> of(type: TypeLiteral<Registrar<T>>): Registry<T>
  }
}