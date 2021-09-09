package com.eloli.inkerbot.core.event

import com.eloli.inkerbot.api.event.*
import com.eloli.inkerbot.api.event.EventListener
import com.eloli.inkerbot.api.plugin.PluginContainer
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors
import javax.inject.Singleton

@Singleton
class InkEventManager : EventManager {
    private val linkedHandlers: MutableMap<Class<Event>, MutableCollection<ImplListenerStruct<Event>>> =
        ConcurrentHashMap()
    private val events: MutableCollection<Class<Event>> = Collections.synchronizedSet(HashSet())
    private val listeners: MutableCollection<ImplListenerStruct<Event>> = Collections.synchronizedSet(HashSet())
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
                    ImplEventListener(plugin, obj, method)
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
        val listenerStruct = ImplListenerStruct(plugin, eventClass, order, beforeModifications, listener)
        listeners.add(listenerStruct as ImplListenerStruct<Event>)
        events.stream()
            .filter { v: Class<out Event> ->
                listenerStruct.eventClass.isAssignableFrom(v)
            }
            .forEach { v: Class<out Event> ->
                listenerStruct.listenerEvents.add(v)
                linkedHandlers[v]!!.add(listenerStruct)
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
                    .filter { v: ImplListenerStruct<Event> -> v.listener is ImplEventListener<*> }
                    .filter { v: ImplListenerStruct<Event> -> (v.listener as ImplEventListener<*>).obj === obj }
                    .filter { v: ImplListenerStruct<Event> -> (v.listener as ImplEventListener<*>).method == method }
                    .forEach { v: ImplListenerStruct<Event> ->
                        listeners.remove(v)
                        v.listenerEvents.forEach {
                                iv: Class<*> -> linkedHandlers[iv]!!.remove(v)
                        }
                    }
            }
    }

    override fun unregisterPluginListeners(plugin: PluginContainer) {
        listeners.stream()
            .filter { v: ImplListenerStruct<Event> -> v.plugin == plugin }
            .forEach { v: ImplListenerStruct<Event> ->
                listeners.remove(v)
                v.listenerEvents.forEach {
                        iv: Class<*> -> linkedHandlers[iv]!!.remove(v)
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
        val listenerStructs: MutableCollection<ImplListenerStruct<Event>> = TreeSet()
        linkedHandlers[eventClass] = listenerStructs
        listeners.stream().filter { v: ImplListenerStruct<Event> -> v.eventClass.isAssignableFrom(eventClass) }
            .forEach { v: ImplListenerStruct<Event> ->
                listenerStructs.add(v)
                v.listenerEvents.add(eventClass)
            }
        for (eventInterface in eventClass.interfaces) {
            registerEvent(eventInterface as Class<Event>)
        }
        if(eventClass.superclass != null) {
            registerEvent(eventClass.superclass as Class<Event>)
        }
    }

    override fun post(event: Event): Boolean {
        poster(event).post()
        return if (event is Cancellable) {
            event.cancelled
        } else {
            true
        }
    }

    fun <T :Event> poster(event: T): EventPoster<T> {
        if (!events.contains(event::class.java as Class<Event>)) {
            registerEvent(event.javaClass)
        }
        return EventPoster<T>(
            event,
            linkedHandlers[event.javaClass as Class<Event>]!!
                .stream()
                .map { obj: ImplListenerStruct<Event> -> ImplListenerStruct::class.java.cast(obj) }
                .collect(Collectors.toList()) as Collection<ImplListenerStruct<T>>
        )
    }

    companion object {
        private val EVENT_CLASS = Event::class.java
    }
}