package com.eloli.inkerbot.core.event

import com.eloli.inkerbot.api.event.Event
import com.eloli.inkerbot.api.event.EventListener
import com.eloli.inkerbot.api.event.Order
import com.eloli.inkerbot.api.plugin.PluginContainer
import java.util.*

class InkListenerStruct<T : Event>(
  val plugin: PluginContainer, val eventClass: Class<T>,
  val order: Order, val beforeModifications: Boolean,
  val listener: EventListener<T>
) : Comparable<InkListenerStruct<T>> {
  val listenerEvents: MutableSet<Class<*>> = HashSet()

  override fun compareTo(another: InkListenerStruct<T>): Int {
    val r = order.compareTo(another.order)
    return if (r == 0) -1 else r
  }

  override fun equals(o: Any?): Boolean {
    return false
  }

  override fun hashCode(): Int {
    return Objects.hash(plugin, eventClass, order, beforeModifications, listener, listenerEvents)
  }

  init {
    Objects.requireNonNull(plugin, "plugin")
    Objects.requireNonNull(eventClass, "eventClass")
    Objects.requireNonNull(order, "order")
    Objects.requireNonNull(listener, "listener")
  }
}