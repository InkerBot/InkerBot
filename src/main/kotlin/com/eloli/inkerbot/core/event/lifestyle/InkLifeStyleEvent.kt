package com.eloli.inkerbot.core.event.lifestyle

import com.eloli.inkerbot.api.event.EventContext
import com.eloli.inkerbot.api.event.lifestyle.LifeStyleEvent

abstract class InkLifeStyleEvent:LifeStyleEvent {
    override val context: EventContext = EventContext.empty()
    class Enable: InkLifeStyleEvent(), LifeStyleEvent.Enable {

    }
}