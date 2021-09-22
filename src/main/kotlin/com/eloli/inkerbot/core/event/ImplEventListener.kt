package com.eloli.inkerbot.core.event

import com.eloli.inkerbot.api.event.Event
import com.eloli.inkerbot.api.event.EventListener
import com.eloli.inkerbot.api.plugin.PluginContainer
import java.lang.reflect.Method

class ImplEventListener<T : Event>(
    val pluginContainer: PluginContainer,
    val obj: Any,
    val method: Method
) : EventListener<T> {
    @Throws(Exception::class)
    override fun handle(event: T) {
        method.invoke(obj, event)
    }
}