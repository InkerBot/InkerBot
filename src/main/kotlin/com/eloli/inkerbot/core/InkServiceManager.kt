package com.eloli.inkerbot.core

import com.eloli.inkerbot.api.Frame
import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.ServiceManager
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.core.event.lifestyle.InkLifeStyleEvent
import com.eloli.inkerbot.core.util.ProxyInjector
import com.google.inject.Binder
import com.google.inject.Injector
import com.google.inject.Module
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class InkServiceManager: ProxyInjector(),ServiceManager {
    @Inject
    private lateinit var eventManager: EventManager
    fun init() {
        registerProxyInjector(
            InkerBot.injector.createChildInjector(object : Module{
                override fun configure(binder: Binder) {
                    eventManager.post(InkLifeStyleEvent.RegisterService(binder))
                }
            })
        )
    }
}