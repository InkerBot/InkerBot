package bot.inker.core.event

import bot.inker.api.InkerBot
import bot.inker.api.event.*
import bot.inker.api.event.EventListener
import bot.inker.api.plugin.PluginContainer
import org.reflections.Reflections
import org.reflections.scanners.Scanner
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import org.slf4j.LoggerFactory
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.net.URL
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors
import javax.inject.Singleton

@Singleton
class InkEventManager : EventManager {
  private val logger = LoggerFactory.getLogger("event")
  private val linkedHandlers: MutableMap<Class<Event>, MutableCollection<InkListenerStruct<Event>>> =
    ConcurrentHashMap()
  private val events: MutableCollection<Class<Event>> = Collections.synchronizedSet(HashSet())
  private val listeners: MutableCollection<InkListenerStruct<Event>> = Collections.synchronizedSet(HashSet())

  override fun scanListeners(plugin: PluginContainer, classLoader: ClassLoader, vararg urls: URL) {
    for (clazz in Reflections(
      ConfigurationBuilder()
        .addClassLoaders(classLoader)
        .addUrls(urls.asList())
    ).getTypesAnnotatedWith(AutoComponent::class.java)) {
      registerListeners(plugin, InkerBot(clazz))
    }
  }

  override fun registerListeners(plugin: PluginContainer, obj: Any) {
    Arrays.stream(obj.javaClass.methods)
      .filter { method: Method ->
        method.returnType == Void.TYPE
            || method.returnType == Object::class.java
      }
      .filter { method: Method -> Modifier.isPublic(Modifier.methodModifiers()) }
      .filter { method: Method ->
        method.getAnnotation(
          EventHandler::class.java
        ) != null
      }
      .filter { method: Method -> method.parameters.size == 1 }
      .filter { method: Method -> EVENT_CLASS.isAssignableFrom(method.parameters[0].type) }
      .forEach { method: Method ->
        val eventAnnotation = method.getAnnotation(
          EventHandler::class.java
        )
        this.registerListener(
          plugin, method.parameters[0].type as Class<Event>,
          eventAnnotation.order, eventAnnotation.beforeModifications,
          InkEventListener(plugin, obj, method)
        )
      }
  }

  override fun <T : Event> registerListener(
    plugin: PluginContainer,
    eventClass: Class<T>,
    listener: EventListener<T>
  ) {
    this.registerListener(plugin, eventClass, Order.DEFAULT, listener)
  }

  override fun <T : Event> registerListener(
    plugin: PluginContainer,
    eventClass: Class<T>,
    order: Order,
    listener: EventListener<T>
  ) {
    this.registerListener(plugin, eventClass, order, false, listener)
  }

  override fun <T : Event> registerListener(
    plugin: PluginContainer,
    eventClass: Class<T>,
    order: Order,
    beforeModifications: Boolean,
    listener: EventListener<T>
  ) {
    val listenerStruct = InkListenerStruct(plugin, eventClass, order, beforeModifications, listener)
    listeners.add(listenerStruct as InkListenerStruct<Event>)
    events.stream()
      .filter { v: Class<out Event> ->
        listenerStruct.eventClass.isAssignableFrom(v)
      }
      .forEach { v: Class<out Event> ->
        listenerStruct.listenerEvents.add(v)
        linkedHandlers[v]!!.add(listenerStruct)
      }
  }

  override fun <T : Event> registerListener(plugin: PluginContainer, eventClass: Class<T>, listener: (T) -> Unit) {
    registerListener(plugin, eventClass, translate(listener))
  }

  override fun <T : Event> registerListener(
    plugin: PluginContainer,
    eventClass: Class<T>,
    order: Order,
    listener: (T) -> Unit
  ) {
    registerListener(plugin, eventClass, order, translate(listener))
  }

  override fun <T : Event> registerListener(
    plugin: PluginContainer,
    eventClass: Class<T>,
    order: Order,
    beforeModifications: Boolean,
    listener: (T) -> Unit
  ) {
    registerListener(plugin, eventClass, order, beforeModifications, translate(listener))
  }

  private fun <T : Event> translate(listener: (T) -> Unit): EventListener<T> {
    return object : EventListener<T> {
      override fun handle(event: T) {
        event.apply(listener)
      }
    }
  }

  override fun unregisterListeners(obj: Any) {
    Arrays.stream(obj.javaClass.methods)
      .filter { method: Method -> method.returnType == Void.TYPE }
      .filter { method: Method -> method.canAccess(obj) }
      .filter { method: Method ->
        method.getAnnotation(
          EventHandler::class.java
        ) != null
      }
      .filter { method: Method -> method.parameters.size == 1 }
      .filter { method: Method -> EVENT_CLASS.isAssignableFrom(method.parameters[0].type) }
      .forEach { method: Method ->
        listeners.stream()
          .filter { v: InkListenerStruct<Event> -> v.listener is InkEventListener<*> }
          .filter { v: InkListenerStruct<Event> -> (v.listener as InkEventListener<*>).obj === obj }
          .filter { v: InkListenerStruct<Event> -> (v.listener as InkEventListener<*>).method == method }
          .forEach { v: InkListenerStruct<Event> ->
            listeners.remove(v)
            v.listenerEvents.forEach { iv: Class<*> ->
              linkedHandlers[iv]!!.remove(v)
            }
          }
      }
  }

  override fun unregisterPluginListeners(plugin: PluginContainer) {
    listeners.stream()
      .filter { v: InkListenerStruct<Event> -> v.plugin == plugin }
      .forEach { v: InkListenerStruct<Event> ->
        listeners.remove(v)
        v.listenerEvents.forEach { iv: Class<*> ->
          linkedHandlers[iv]!!.remove(v)
        }
      }
  }

  fun registerEvent(clazz: Class<Event>) {
    if (clazz == null || !EVENT_CLASS.isAssignableFrom(clazz)) {
      return
    }
    val eventClass = clazz
    if (events.contains(eventClass)) {
      return
    }
    events.add(eventClass)
    val listenerStructs: MutableCollection<InkListenerStruct<Event>> = TreeSet()
    linkedHandlers[eventClass] = listenerStructs
    listeners.stream().filter { v: InkListenerStruct<Event> -> v.eventClass.isAssignableFrom(eventClass) }
      .forEach { v: InkListenerStruct<Event> ->
        listenerStructs.add(v)
        v.listenerEvents.add(eventClass)
      }
    for (eventInterface in eventClass.interfaces) {
      registerEvent(eventInterface as Class<Event>)
    }
    if (eventClass.superclass != null) {
      registerEvent(eventClass.superclass as Class<Event>)
    }
  }

  override fun post(event: Event): Boolean {
    logger.debug("{}", event)
    poster(event).post()
    return if (event is Cancellable) {
      event.cancelled
    } else {
      true
    }
  }

  fun <T : Event> poster(event: T): EventPoster<T> {
    if (!events.contains(event::class.java as Class<Event>)) {
      registerEvent(event.javaClass)
    }
    return EventPoster<T>(
      event,
      linkedHandlers[event.javaClass as Class<Event>]!!
        .stream()
        .map { obj: InkListenerStruct<Event> -> InkListenerStruct::class.java.cast(obj) }
        .collect(Collectors.toList()) as Collection<InkListenerStruct<T>>
    )
  }

  companion object {
    private val EVENT_CLASS = Event::class.java
  }
}