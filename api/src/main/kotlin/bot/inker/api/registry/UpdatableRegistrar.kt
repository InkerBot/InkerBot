package bot.inker.api.registry

import bot.inker.api.InkerBot
import bot.inker.api.util.Identity
import bot.inker.api.util.ResourceKey

interface UpdatableRegistrar<T, U : T, V : Cloneable> : Registrar<T> {
  fun update(identity: Identity, command: (V) -> Unit): U

  companion object {
    fun <T, U : T, V : Cloneable> of(
      registry: Registry<T>,
      key: ResourceKey,
      registrarClass: Class<T>,
      realClass: Class<U>,
      recordClass: Class<V>
    ): UpdatableRegistrar<T, U, V> {
      return InkerBot(Factory::class.java).of(
        registry,
        key,
        registrarClass,
        realClass,
        recordClass
      )
    }
  }

  interface Factory {
    fun <T, U : T, V : Cloneable> of(
      registry: Registry<T>,
      key: ResourceKey,
      registrarClass: Class<T>,
      realClass: Class<U>,
      recordClass: Class<V>
    ): UpdatableRegistrar<T, U, V>
  }
}