package com.eloli.inkerbot.core

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.registry.Registrar
import com.eloli.inkerbot.api.registry.Registry
import com.eloli.inkerbot.api.service.DatabaseService
import com.eloli.inkerbot.api.util.ResourceKey
import com.eloli.inkerbot.core.event.lifestyle.InkLifeStyleEvent
import com.eloli.inkerbot.core.util.StaticEntryUtil
import com.google.inject.Guice
import com.google.inject.TypeLiteral
import org.apache.log4j.BasicConfigurator
import org.ktorm.database.Database

fun main() {
    BasicConfigurator.configure()
    val inkerBotModule = InkerBotModule()
    val injector = Guice.createInjector(
        inkerBotModule
    )
    StaticEntryUtil.applyInjector(InkerBot::class.java.classLoader, injector)

    InkerBot.injector.getInstance(EventManager::class.java).registerListeners(
        InkerBot.injector.getInstance(InkerBotPluginContainer::class.java),
        inkerBotModule
    )

    // Load Plugins
    InkerBot.injector.getInstance(InkFrame::class.java).init()

    // Enable
    InkerBot.injector.getInstance(EventManager::class.java).post(InkLifeStyleEvent.Enable())

    // Register service
    InkerBot.injector.getInstance(InkServiceManager::class.java).init()

    InkerBot.serviceManager.getInstance(Database::class.java)
}