package com.eloli.inkerbot.iirose

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.event.EventHandler
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.event.lifestyle.LifeStyleEvent
import com.eloli.inkerbot.api.plugin.JvmPlugin
import com.eloli.inkerbot.api.plugin.PluginContainer
import com.eloli.inkerbot.iirose.config.IbConfig
import com.eloli.inkerbot.iirose.config.IbConfigProvider
import com.eloli.inkerbot.iirose.event.IbGroupMessageEvent
import com.eloli.inkerbot.iirose.event.IbSendPrivateMessage
import com.eloli.inkerbot.iirose.event.IbSendRoomMessage
import com.google.inject.Binder
import javax.inject.Inject

class IiroseCore:JvmPlugin {
    override fun configure(binder: Binder) {
        binder.bind(IbConfig::class.java).toProvider(IbConfigProvider::class.java)
    }
    @Inject
    private lateinit var pluginContainer: PluginContainer

    @Inject
    private lateinit var connection: IbConnection

    @Inject
    private lateinit var eventManager: EventManager

    @EventHandler
    fun onBoot(event: LifeStyleEvent.Enable){
        connection.onBoot()
        eventManager.registerListeners(pluginContainer,
            InkerBot.injector.getInstance(IbGroupMessageEvent.Resolver::class.java))
        eventManager.registerListeners(pluginContainer,
            InkerBot.injector.getInstance(IbSendRoomMessage.Resolver::class.java))
        eventManager.registerListeners(pluginContainer,
            InkerBot.injector.getInstance(IbSendPrivateMessage.Resolver::class.java))
    }

    @EventHandler
    fun onEvent(event: IbGroupMessageEvent){
        event.sender.sendMessage(event.message)
    }
}