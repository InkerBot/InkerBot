package com.eloli.inkerbot.core.event.lifestyle

import com.eloli.inkerbot.api.event.EventContext
import com.eloli.inkerbot.api.event.lifestyle.LifeStyleEvent
import com.google.inject.Binder
import javax.inject.Singleton

@Singleton
abstract class InkLifeStyleEvent:LifeStyleEvent {
    override val context: EventContext = EventContext.empty()
    class Enable: InkLifeStyleEvent(), LifeStyleEvent.Enable {

    }
    class RegisterService(override val binder: Binder): InkLifeStyleEvent(), LifeStyleEvent.RegisterService {

    }
}