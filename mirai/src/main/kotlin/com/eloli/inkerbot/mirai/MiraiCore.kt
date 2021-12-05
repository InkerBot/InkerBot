package com.eloli.inkerbot.mirai

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.event.EventHandler
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.event.lifestyle.LifecycleEvent
import com.eloli.inkerbot.api.event.message.MessageEvent
import com.eloli.inkerbot.api.model.message.PlainTextComponent
import com.eloli.inkerbot.api.plugin.JvmPlugin
import com.eloli.inkerbot.api.plugin.PluginContainer
import com.google.inject.Binder
import com.google.inject.Inject
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.utils.LoggerAdapters.asMiraiLogger
import org.slf4j.Logger

class MiraiCore : JvmPlugin {
  override fun configure(binder: Binder) {
    binder.bind(MiraiConfig::class.java).toProvider(MiraiConfigProvider::class.java)
  }

  @Inject
  private lateinit var config: MiraiConfig

  @Inject
  private lateinit var eventManager: EventManager

  @Inject
  private lateinit var plugin: PluginContainer

  @Inject
  private lateinit var logger: Logger

  @EventHandler
  fun onEnable(e: LifecycleEvent.Enable) {
    runBlocking {
      var bot = BotFactory.newBot(config.qqNumber.toLong(), config.qqPassword) {
        fileBasedDeviceInfo(plugin.dataPath.resolve("device.json").toString())
        cacheDir = plugin.dataPath.toFile()
        botLoggerSupplier = { logger.asMiraiLogger() }
        networkLoggerSupplier = { logger.asMiraiLogger() }
      }.alsoLogin()
      MiraiHandler.register(bot)
      bot.eventChannel.subscribeAlways<Event> {
        logger.debug("onEvent: {}", this)
        eventManager.post(MiraiBoxEvent(this))
      }
    }

    InkerBot.eventManager.registerListener(plugin, MessageEvent::class.java) {
      if (it.message is PlainTextComponent
        && (it.message as PlainTextComponent).context.startsWith("/inkerbot:表白")
      ) {
        it.sendMessage(PlainTextComponent.of("谢谢！"))
      }
    }
  }
}