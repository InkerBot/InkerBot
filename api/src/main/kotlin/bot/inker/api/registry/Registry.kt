package bot.inker.api.registry

import bot.inker.api.util.Identity
import bot.inker.api.util.ResourceKey
import com.google.inject.TypeLiteral
import java.util.*

interface Registry<T> {
  fun register(key: ResourceKey, registrar: Registrar<T>)

  fun generate(key: ResourceKey): Identity
  fun get(key: ResourceKey): Registrar<T>
  fun get(identity: Identity): Optional<T>

  companion object {
    fun <T> of(type: TypeLiteral<Registrar<T>>): Registry<T> {
      return bot.inker.api.InkerBot.injector.getInstance(Factory::class.java).of(type)
    }
  }

  interface Factory {
    fun <T> of(type: TypeLiteral<Registrar<T>>): Registry<T>
  }
}