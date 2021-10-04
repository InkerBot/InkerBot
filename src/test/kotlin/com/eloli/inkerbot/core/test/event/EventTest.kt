package com.eloli.inkerbot.core.test.event

import com.eloli.inkerbot.api.event.EventListener
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.core.InkerBotPluginContainer
import com.eloli.inkerbot.core.test.InjectTest
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@InjectTest
class EventTest {
    @Inject
    private lateinit var eventManager: EventManager

    @Inject
    private lateinit var plugin: InkerBotPluginContainer

    @Test
    fun postEvent() {
        eventManager.post(AEvent())
    }

    @Test
    fun listenEvent() {
        val called: AtomicBoolean = AtomicBoolean(false)
        eventManager.registerListener(plugin, AEvent::class.java, object : EventListener<AEvent> {
            override fun handle(event: AEvent) {
                called.set(true)
            }
        })
        eventManager.post(AEvent())
        assert(called.get())
    }
}