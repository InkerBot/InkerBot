package bot.inker.mirai

import bot.inker.api.InkerBot
import bot.inker.api.event.EventHandler
import bot.inker.api.event.EventManager
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.event.message.MessageEvent
import bot.inker.api.model.message.PlainTextComponent
import bot.inker.api.plugin.JvmPlugin
import bot.inker.api.plugin.PluginContainer
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