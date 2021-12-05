package com.eloli.inkerbot.api.registry

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.util.Identity

interface UpdatableRegistrar<T, U : T, V : Cloneable> : Registrar<T> {
  fun update(identity: Identity, command: (V) -> Unit): U

  companion object {
    fun <T, U : T, V : Cloneable> of(
      registrarClass: Class<T>,
      realClass: Class<U>,
      recordClass: Class<V>
    ): UpdatableRegistrar<T, U, V> {
      return InkerBot.injector.getInstance(Factory::class.java).of(
        registrarClass,
        realClass,
        recordClass
      )
    }
  }

  interface Factory {
    fun <T, U : T, V : Cloneable> of(
      registrarClass: Class<T>,
      realClass: Class<U>,
      recordClass: Class<V>
    ): UpdatableRegistrar<T, U, V>
  }
}