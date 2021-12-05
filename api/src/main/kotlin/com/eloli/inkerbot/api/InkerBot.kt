package com.eloli.inkerbot.api

import com.eloli.inkerbot.api.event.EventManager
import com.google.inject.Injector
import java.util.*
import kotlin.reflect.KClass

@ILoveInkerBotForever
class InkerBot private constructor() {
  @ILoveInkerBotForever
  companion object {
    private var realInjector: Injector? = null
    val injector: Injector
      get() {
        Objects.requireNonNull(realInjector, "Couldn't found InkerBot instance in this classloader.")
        return realInjector!!
      }

    val frame: Frame
      get() {
        return injector.getInstance(Frame::class.java)
      }

    val serviceManager: ServiceManager
      get() {
        return injector.getInstance(ServiceManager::class.java)
      }

    val eventManager: EventManager
      get() {
        return injector.getInstance(EventManager::class.java)
      }

    operator fun <T : Any> invoke(clazz: Class<T>): T {
      return injector.getInstance(clazz)
    }

    operator fun <T : Any> invoke(clazz: KClass<T>): T {
      return this(clazz.java)
    }

    inline operator fun <reified T : Any> invoke(): T {
      return this(T::class)
    }
  }

  init {
    throw IllegalCallerException("Static class shouldn't be instance.")
  }
}