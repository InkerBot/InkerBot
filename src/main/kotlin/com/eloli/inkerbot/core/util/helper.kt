package com.eloli.inkerbot.core.util

import com.google.inject.Injector
import kotlin.reflect.KClass

operator fun <T : Any> Injector.invoke(clazz: KClass<T>): T {
  return this.getInstance(clazz.java)
}

operator fun <T : Any> Injector.invoke(clazz: Class<T>): T {
  return this.getInstance(clazz)
}

inline operator fun <reified T : Any> Injector.invoke(): T {
  return this(T::class)
}