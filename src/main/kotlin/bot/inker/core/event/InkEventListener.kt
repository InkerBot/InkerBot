package bot.inker.core.event

import bot.inker.api.event.Event
import bot.inker.api.event.EventListener
import bot.inker.api.plugin.PluginContainer
import java.lang.reflect.Method

class InkEventListener<T : Event>(
  val pluginContainer: PluginContainer,
  val obj: Any,
  val method: Method
) : EventListener<T> {
  @Throws(Exception::class)
  override fun handle(event: T) {
    method.invoke(obj, event)
  }
}