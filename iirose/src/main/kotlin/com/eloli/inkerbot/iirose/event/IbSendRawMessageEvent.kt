package com.eloli.inkerbot.iirose.event

import com.eloli.inkerbot.api.event.Event
import com.eloli.inkerbot.api.event.EventContext

class IbSendRawMessageEvent(val message: String) : Event {
    override val context: EventContext = EventContext.empty()
}