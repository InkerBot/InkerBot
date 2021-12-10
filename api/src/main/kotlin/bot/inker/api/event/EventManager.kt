package bot.inker.api.event

import bot.inker.api.plugin.PluginContainer

interface EventManager {
  fun registerListeners(plugin: PluginContainer, obj: Any)
  fun <T : Event> registerListener(plugin: PluginContainer, eventClass: Class<T>, listener: EventListener<T>)
  fun <T : Event> registerListener(
    plugin: PluginContainer,
    eventClass: Class<T>,
    order: Order,
    listener: EventListener<T>
  )

  fun <T : Event> registerListener(
    plugin: PluginContainer, eventClass: Class<T>, order: Order, beforeModifications: Boolean,
    listener: EventListener<T>
  )

  fun <T : Event> registerListener(plugin: PluginContainer, eventClass: Class<T>, listener: (T) -> Unit)
  fun <T : Event> registerListener(
    plugin: PluginContainer,
    eventClass: Class<T>,
    order: Order,
    listener: (T) -> Unit
  )

  fun <T : Event> registerListener(
    plugin: PluginContainer, eventClass: Class<T>, order: Order, beforeModifications: Boolean,
    listener: (T) -> Unit
  )

  fun unregisterListeners(obj: Any)
  fun unregisterPluginListeners(plugin: PluginContainer)
  fun post(event: Event): Boolean
}