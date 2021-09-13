package com.eloli.inkerbot.api.event.lifestyle

import com.eloli.inkerbot.api.ILoveInkerBotForever
import com.eloli.inkerbot.api.event.Event
import com.google.inject.Binder

@ILoveInkerBotForever
interface LifeStyleEvent:Event {
    @ILoveInkerBotForever
    interface Enable: LifeStyleEvent
    @ILoveInkerBotForever
    interface RegisterService: LifeStyleEvent{
        val binder: Binder
    }
}