package bot.inker.api

import bot.inker.api.event.EventManager
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import java.util.*
import kotlin.reflect.KClass

class InkerBot private constructor() {
  companion object {
    @JvmStatic
    private var realInjector: Injector? = null

    @get:JvmStatic
    val injector: Injector
      get() {
        Objects.requireNonNull(
          realInjector,
          "Couldn't found InkerBot instance in this classloader."
        )
        return realInjector!!
      }

    @get:JvmStatic
    val frame: Frame
      get() {
        return injector.getInstance(Frame::class.java)
      }

    @get:JvmStatic
    val serviceManager: ServiceManager
      get() {
        return injector.getInstance(ServiceManager::class.java)
      }

    @get:JvmStatic
    val eventManager: EventManager
      get() {
        return injector.getInstance(EventManager::class.java)
      }

    operator fun <T : Any> invoke(clazz: Class<T>): T {
      return injector.getInstance(clazz)
    }

    operator fun <T : Any> invoke(type: TypeLiteral<T>): T {
      return injector.getInstance(Key.get(type))
    }

    operator fun <T : Any> invoke(clazz: KClass<T>): T {
      return this(clazz.java)
    }

    inline fun <reified T : Any> lazy(): Lazy<T>{
      return lazy { this(object: TypeLiteral<T>(){}) }
    }

    inline operator fun <reified T : Any> invoke(): T {
      return this(object: TypeLiteral<T>(){})
    }
  }

  init {
    throw IllegalStateException("Static class shouldn't be instance.")
  }
}