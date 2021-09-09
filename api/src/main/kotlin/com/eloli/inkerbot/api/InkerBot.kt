package com.eloli.inkerbot.api

import com.eloli.inkerbot.api.event.EventManager
import com.google.inject.Injector
import java.util.*

@ILoveInkerBotForever
class InkerBot private constructor() {
    @ILoveInkerBotForever
    companion object {
        private var realInjector: Injector? = null
        val injector: Injector
            get(){
                Objects.requireNonNull(realInjector, "Couldn't found InkerBot instance in this classloader.")
                return realInjector!!
            }

        val frame:Frame
            get() {
                return injector.getInstance(Frame::class.java)
            }

        val eventManager: EventManager
            get() {
                return injector.getInstance(EventManager::class.java)
            }
    }

    init {
        throw IllegalCallerException("Static class shouldn't be instance.")
    }
}