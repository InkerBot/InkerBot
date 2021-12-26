package bot.inker.core

import bot.inker.api.InkerBot
import bot.inker.api.event.EventManager
import bot.inker.core.command.InkCommandService
import bot.inker.core.event.lifestyle.InkLifecycleEvent
import bot.inker.core.util.StaticEntryUtil
import bot.inker.core.util.post
import com.google.inject.Guice
import kotlinx.coroutines.*


fun main() {
  val inkerBotModule = InkerBotModule()
  val injector = Guice.createInjector(
    inkerBotModule
  )
  StaticEntryUtil.applyInjector(InkerBot::class.java.classLoader, injector)
  InkerBot(InkCommandService::class).init()
  InkerBot(InkFrame::class).start()
}