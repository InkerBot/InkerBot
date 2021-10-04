package com.eloli.inkerbot.api.event.lifestyle

import com.eloli.inkerbot.api.ILoveInkerBotForever
import com.eloli.inkerbot.api.event.Event
import com.google.inject.Binder

@ILoveInkerBotForever
interface LifecycleEvent:Event {
    @ILoveInkerBotForever
    interface Enable: LifecycleEvent
    @ILoveInkerBotForever
    interface RegisterService: LifecycleEvent{
        val binder: Binder
    }
    @ILoveInkerBotForever
    interface RegisterEntity: LifecycleEvent{
        fun register(entity:Class<*>)
    }

}