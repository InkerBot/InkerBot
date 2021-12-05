package com.eloli.inkerbot.core

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.service.CommandService
import com.eloli.inkerbot.core.command.InkCommandService
import com.eloli.inkerbot.core.event.InkConsoleMessageEvent
import com.eloli.inkerbot.core.event.lifestyle.InkLifecycleEvent
import com.eloli.inkerbot.core.util.StaticEntryUtil
import com.google.inject.Guice
import org.apache.log4j.BasicConfigurator

fun main() {
  val inkerBotModule = InkerBotModule()
  val injector = Guice.createInjector(
    inkerBotModule
  )
  StaticEntryUtil.applyInjector(InkerBot::class.java.classLoader, injector)
  InkerBot(InkCommandService::class).init()

  BasicConfigurator.configure()

  InkerBot(EventManager::class).registerListeners(
    InkerBot(InkerBotPluginContainer::class),
    inkerBotModule
  )

  InkerBot(EventManager::class).registerListeners(
    InkerBot(InkerBotPluginContainer::class),
    InkerBot(InkCommandService::class)
  )

  // Load Plugins
  InkerBot(InkFrame::class).init()

  // Enable
  InkerBot(EventManager::class).post(InkLifecycleEvent.Enable())

  // Register service
  InkerBot(InkServiceManager::class).init()

  InkerBot(InkCommandService::class).loop()

  // Use normal exit
  InkerBot(CommandService::class).dispatcher.execute("stop", InkConsoleMessageEvent("stop"))
}