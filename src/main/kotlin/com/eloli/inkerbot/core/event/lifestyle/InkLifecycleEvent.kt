package com.eloli.inkerbot.core.event.lifestyle

import com.eloli.inkerbot.api.event.EventContext
import com.eloli.inkerbot.api.event.lifestyle.LifecycleEvent
import com.google.inject.Binder
import javax.inject.Singleton

@Singleton
abstract class InkLifecycleEvent:LifecycleEvent {
    override val context: EventContext = EventContext.empty()
    class Enable: InkLifecycleEvent(), LifecycleEvent.Enable {

    }
    class RegisterService(override val binder: Binder): InkLifecycleEvent(), LifecycleEvent.RegisterService {

    }
    class RegisterEntity(private val action: (Class<*>)->Unit):InkLifecycleEvent(), LifecycleEvent.RegisterEntity{
        override fun register(entity: Class<*>) {
            action.invoke(entity)
        }
    }
}