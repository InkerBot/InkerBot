package bot.inker.core.util

import bot.inker.api.InkerBot
import bot.inker.api.event.Event
import bot.inker.api.event.EventManager
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

inline fun <reified T : Event> T.post(): T {
  InkerBot(EventManager::class).post(this)
  return this
}