package com.eloli.inkerbot.core.test.event

import com.eloli.inkerbot.api.event.Cancellable
import com.eloli.inkerbot.api.event.Event
import com.eloli.inkerbot.api.event.EventContext

class AEvent : Event, Cancellable {
    override var cancelled: Boolean = false

    override val context: EventContext = EventContext.builder().build()
}