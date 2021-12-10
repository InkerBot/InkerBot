package bot.inker.api.event

import bot.inker.api.builder.AbstractBuilder
import java.util.*
import java.util.function.Supplier

class EventContext private constructor(values: Map<EventContextKey<*>, Any>) {
  private val entries: Map<EventContextKey<*>, *>

  @Suppress("UNCHECKED_CAST")
  operator fun <T> get(key: EventContextKey<T>): Optional<T> {
    Objects.requireNonNull(key, "EventContextKey cannot be null")
    return Optional.ofNullable(entries[key] as T)
  }

  @Suppress("UNCHECKED_CAST")
  operator fun <T> get(key: Supplier<EventContextKey<T>>): Optional<T> {
    Objects.requireNonNull(key, "EventContextKey cannot be null")
    return Optional.ofNullable(entries[key.get()] as T)
  }

  fun <T> require(key: EventContextKey<T>): T {
    val optional = this[key]
    if (optional.isPresent) {
      return optional.get()
    }
    throw NoSuchElementException(String.format("Could not retrieve value for key '%s'", key.toString()))
  }

  fun <T> require(key: Supplier<EventContextKey<T>>): T {
    val optional = this.get(key)
    if (optional.isPresent) {
      return optional.get()
    }
    throw NoSuchElementException(String.format("Could not retrieve value for key '%s'", key.get().toString()))
  }

  fun containsKey(key: EventContextKey<*>): Boolean {
    return entries.containsKey(key)
  }

  fun containsKey(key: Supplier<out EventContextKey<*>>): Boolean {
    return entries.containsKey(key.get())
  }

  fun keySet(): Set<EventContextKey<*>> {
    return entries.keys
  }

  fun asMap(): Map<EventContextKey<*>, *> {
    return entries
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) {
      return true
    }
    if (other !is EventContext) {
      return false
    }
    for ((key, value) in entries) {
      val o = other.entries[key]
      if (value != o) {
        return false
      }
    }
    return true
  }

  override fun hashCode(): Int {
    return entries.hashCode()
  }

  class Builder : AbstractBuilder<EventContext> {
    private val entries: MutableMap<EventContextKey<*>, Any> = HashMap()
    fun <T> add(key: EventContextKey<T>, value: T): Builder {
      Objects.requireNonNull(value, "Context object cannot be null!")
      require(!entries.containsKey(key)) { "Duplicate context keys: $key" }
      entries[key] = value!!
      return this
    }

    fun <T> add(key: Supplier<EventContextKey<T>>, value: T): Builder {
      Objects.requireNonNull(value, "Context object cannot be null!")
      val suppliedKey = key.get()
      Objects.requireNonNull(suppliedKey, "Supplied key cannot be null!")
      require(!entries.containsKey(suppliedKey)) { "Duplicate context keys!" }
      entries[suppliedKey] = value!!
      return this
    }

    override fun build(): EventContext {
      return EventContext(entries)
    }
  }

  companion object {
    private val EMPTY_CONTEXT = EventContext(HashMap())
    fun empty(): EventContext {
      return EMPTY_CONTEXT
    }

    fun of(entries: Map<EventContextKey<*>, Any>): EventContext {
      Objects.requireNonNull(entries, "Context entries cannot be null")
      for ((_, value) in entries) {
        Objects.requireNonNull(value, "Entries cannot contain null values")
      }
      return EventContext(entries)
    }

    fun builder(): Builder {
      return Builder()
    }
  }

  init {
    entries = HashMap(values)
  }
}