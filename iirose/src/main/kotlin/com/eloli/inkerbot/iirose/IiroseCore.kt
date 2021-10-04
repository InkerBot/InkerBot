package com.eloli.inkerbot.iirose

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.event.EventHandler
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.event.lifestyle.LifecycleEvent
import com.eloli.inkerbot.api.event.message.MessageEvent
import com.eloli.inkerbot.api.model.Group
import com.eloli.inkerbot.api.model.Member
import com.eloli.inkerbot.api.plugin.JvmPlugin
import com.eloli.inkerbot.api.plugin.PluginContainer
import com.eloli.inkerbot.api.registry.Registrar
import com.eloli.inkerbot.api.service.DatabaseService
import com.eloli.inkerbot.iirose.config.IbConfig
import com.eloli.inkerbot.iirose.config.IbConfigProvider
import com.eloli.inkerbot.iirose.event.IbGroupMessageEvent
import com.eloli.inkerbot.iirose.event.IbPrivateMessageEvent
import com.eloli.inkerbot.iirose.event.IbSendPrivateMessage
import com.eloli.inkerbot.iirose.event.IbSendRoomMessage
import com.eloli.inkerbot.iirose.registry.IbGroupRegistrar
import com.eloli.inkerbot.iirose.registry.IbMemberRegistrar
import com.google.inject.Binder
import com.google.inject.TypeLiteral
import com.google.inject.name.Names
import javax.inject.Inject

class IiroseCore:JvmPlugin {
    override fun configure(binder: Binder) {
        binder.bind(IbConfig::class.java).toProvider(IbConfigProvider::class.java)
    }
    @Inject
    private lateinit var databaseService: DatabaseService

    @Inject
    private lateinit var pluginContainer: PluginContainer

    @Inject
    private lateinit var connection: IbConnection

    @Inject
    private lateinit var eventManager: EventManager

    @EventHandler
    fun onBoot(event: LifecycleEvent.Enable){
        connection.onBoot()
        eventManager.registerListeners(pluginContainer,
            InkerBot.injector.getInstance(IbGroupMessageEvent.Resolver::class.java))
        eventManager.registerListeners(pluginContainer,
            InkerBot.injector.getInstance(IbPrivateMessageEvent.Resolver::class.java))
        eventManager.registerListeners(pluginContainer,
            InkerBot.injector.getInstance(IbSendRoomMessage.Resolver::class.java))
        eventManager.registerListeners(pluginContainer,
            InkerBot.injector.getInstance(IbSendPrivateMessage.Resolver::class.java))
    }

    @EventHandler
    fun onRegisterService(event: LifecycleEvent.RegisterService){
        event.binder.bind(object : TypeLiteral<Registrar<Member>>() {}).annotatedWith(Names.named("iirose:member")).to(IbMemberRegistrar::class.java)
        event.binder.bind(object : TypeLiteral<Registrar<Group>>() {}).annotatedWith(Names.named("iirose:room")).to(IbGroupRegistrar::class.java)
    }

    @EventHandler
    fun onRegisterEntity(event: LifecycleEvent.RegisterEntity){
        event.register(IbGroupRegistrar.IbGroupRecord::class.java)
        event.register(IbMemberRegistrar.IbMemberRecord::class.java)
    }

    @EventHandler
    fun onEvent(event: MessageEvent){
        event.sender.sendMessage(event.message)
    }
}