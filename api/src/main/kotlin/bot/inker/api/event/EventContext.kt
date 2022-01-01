package bot.inker.api.event

import bot.inker.api.InkerBot
import java.util.*
import java.util.stream.Stream

interface EventContext:Iterable<EventContextEntry<Any>> {
  fun <T:Any> get(key: EventContextKey<T>):Optional<T>

  fun <T:Any> getOrSave(key: EventContextKey<T>, value: T): T
  fun <T:Any> getOrSave(key: EventContextKey<T>, getter: () -> T): T
  fun <T:Any> getOrSaveOptional(key: EventContextKey<T>, getter: () -> Optional<T>): Optional<T>
  fun <T:Any> getOrSaveNullable(key: EventContextKey<T>, getter: () -> T?): Optional<T>

  fun <T:Any> getOr(key: EventContextKey<T>, value: T): T
  fun <T:Any> getOr(key: EventContextKey<T>, getter: () -> T): T
  fun <T:Any> getOrOptional(key: EventContextKey<T>, getter: () -> Optional<T>): Optional<T>
  fun <T:Any> getOrNullable(key: EventContextKey<T>, getter: () -> T?): Optional<T>

  fun <T:Any> require(key: EventContextKey<T>):T

  fun stream():Stream<EventContextEntry<Any>>
  fun clone():EventContext

  interface Factory{
    fun empty():EventContext
    fun of(vararg values:EventContextEntry<Any>):EventContext
  }
  companion object{
    fun empty():EventContext{
      return InkerBot(Factory::class).empty()
    }
    fun of(vararg values:EventContextEntry<Any>):EventContext {
      return InkerBot(Factory::class).of(*values)
    }
  }
}