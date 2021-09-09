package com.eloli.inkerbot.api.event.lifestyle

import com.eloli.inkerbot.api.ILoveInkerBotForever
import com.eloli.inkerbot.api.event.Event

@ILoveInkerBotForever
interface LifeStyleEvent:Event {
    @ILoveInkerBotForever
    interface Enable: LifeStyleEvent
}