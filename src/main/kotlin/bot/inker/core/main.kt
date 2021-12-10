package bot.inker.core

import bot.inker.api.InkerBot
import bot.inker.api.event.EventManager
import bot.inker.api.service.CommandService
import bot.inker.core.command.InkCommandService
import bot.inker.core.event.InkConsoleMessageEvent
import bot.inker.core.event.lifestyle.InkLifecycleEvent
import bot.inker.core.setting.InkSetting
import bot.inker.core.util.StaticEntryUtil
import com.google.inject.Guice
import org.apache.log4j.BasicConfigurator
import org.apache.log4j.LogManager
import org.apache.log4j.PropertyConfigurator
import java.util.*

fun main() {
  val inkerBotModule = InkerBotModule()
  val injector = Guice.createInjector(
    inkerBotModule
  )
  StaticEntryUtil.applyInjector(InkerBot::class.java.classLoader, injector)
  InkerBot(InkCommandService::class).init()

  val config: InkSetting = InkerBot()

  val configurator = PropertyConfigurator()
  val properties = Properties()
  properties["log4j.rootLogger"] = "DEBUG,console,debug,file"

  properties["log4j.appender.console"] = "org.apache.log4j.ConsoleAppender"
  properties["log4j.appender.console.Threshold"] = if (config.debug) {
    "DEBUG"
  } else {
    "INFO"
  }
  properties["log4j.appender.console.layout"] = "org.apache.log4j.PatternLayout"
  properties["log4j.appender.console.layout.ConversionPattern"] = "%r [%t] %p %c %x - %m%n"

  properties["log4j.appender.debug"] = "org.apache.log4j.RollingFileAppender"
  properties["log4j.appender.debug.Threshold"] = "DEBUG"
  properties["log4j.appender.debug.File"] = "logs/debug.log"
  properties["log4j.appender.debug.MaxFileSize"] = "10MB"
  properties["log4j.appender.debug.MaxBackupIndex"] = "0"
  properties["log4j.appender.debug.layout"] = "org.apache.log4j.PatternLayout"
  properties["log4j.appender.debug.layout.ConversionPattern"] = "%r [%t] %p %c %x - %m%n"

  properties["log4j.appender.file"] = "org.apache.log4j.DailyRollingFileAppender"
  properties["log4j.appender.file.Threshold"] = "INFO"
  properties["log4j.appender.file.File"] = "logs/latest.log"
  properties["log4j.appender.file.DatePattern"] = "'.'yyyy-MM-dd"
  properties["log4j.appender.file.layout"] = "org.apache.log4j.PatternLayout"
  properties["log4j.appender.file.layout.ConversionPattern"] = "%r [%t] %p %c %x - %m%n"
  configurator.doConfigure(properties, LogManager.getLoggerRepository())

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

  System.exit(0)
}