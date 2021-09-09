package com.eloli.inkerbot.core

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.core.util.StaticEntryUtil
import com.google.inject.Guice
import org.apache.log4j.BasicConfigurator

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
    InkerBot.injector.getInstance(InkFrame::class.java).init()
    while (true){
        Thread.sleep(1000)
    }
}