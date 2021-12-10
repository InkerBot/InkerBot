package bot.inker.core.event

import bot.inker.api.event.Event
import bot.inker.api.event.Modifiable
import bot.inker.api.event.Order
import java.lang.reflect.InvocationTargetException
import java.util.*

class EventPoster<T : Event>(private val event: T, private val listeners: Collection<InkListenerStruct<T>>) {
  fun post() {
    Arrays.stream(Order.values()).forEach { order: Order -> this.post(order) }
  }

  fun post(order: Order) {
    for (listener in listeners) {
      if (listener.order === order) {
        if (event is Modifiable
          && (event as Modifiable).isModified
          && listener.beforeModifications
        ) {
          continue
        }
        try {
          listener.listener.handle(event)
        } catch (exception: Exception) {
          listener.plugin.logger.warn(
            String.format("Can't pass %s to %s.", event.javaClass.simpleName, listener.plugin.name),
            if (exception is InvocationTargetException) {
              exception.targetException
            } else {
              exception
            }
          )
        }
      }
    }
  }
}