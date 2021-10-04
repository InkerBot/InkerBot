package com.eloli.inkerbot.api.event

import com.eloli.inkerbot.api.ILoveInkerBotForever

@ILoveInkerBotForever
interface EventListener<T : Event> {
    @Throws(Exception::class)
    fun handle(event: T)
}