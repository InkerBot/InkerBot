package com.eloli.inkerbot.core.util

import com.eloli.inkerbot.api.util.ResourceKey
import javax.inject.Singleton

class InkResourceKey(override val namespace: String, override val value: String) : ResourceKey {
  companion object {
    private val NAME_FORMAT: Regex = Regex("[a-z]+")
    private val KEY_FORMAT: Regex = Regex("[a-z]+:[a-z]+")
  }

  override fun toString(): String {
    return "$namespace:$value"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as InkResourceKey

    if (namespace != other.namespace) return false
    if (value != other.value) return false

    return true
  }

  override fun hashCode(): Int {
    var result = namespace.hashCode()
    result = 31 * result + value.hashCode()
    return result
  }


  @Singleton
  class Factory : ResourceKey.Factory {
    override fun of(namespace: String, value: String): ResourceKey {
      return InkResourceKey(namespace, value)
    }

    override fun resolve(formatted: String): ResourceKey {
      if (!formatted.matches(KEY_FORMAT)) {
        throw IllegalArgumentException("Key with invalid format. It should be like aaa:bbb, but $formatted input.")
      }
      val parts = formatted.split(":")
      return of(parts[0], parts[1])
    }

  }
}