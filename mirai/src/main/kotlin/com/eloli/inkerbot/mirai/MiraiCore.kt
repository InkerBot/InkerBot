package com.eloli.inkerbot.mirai

import com.eloli.inkerbot.api.event.EventHandler
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.event.lifestyle.LifeStyleEvent
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
import org.slf4j.LoggerFactory

class MiraiCore:JvmPlugin {
    override fun configure(binder: Binder) {
        binder.bind(MiraiConfig::class.java).toProvider(MiraiConfigProvider::class.java)
    }

    @Inject
    private lateinit var config: MiraiConfig
    @Inject
    private lateinit var eventManager: EventManager
    @Inject
    private lateinit var plugin: PluginContainer
    private var logger: Logger = LoggerFactory.getLogger("mirai")

    @EventHandler
    fun onEnable(event:LifeStyleEvent.Enable){
        runBlocking {
            var bot = BotFactory.newBot(config.qqNumber.toLong(), config.qqPassword){
                fileBasedDeviceInfo(plugin.dataPath.resolve("device.json").toString())
                cacheDir = plugin.dataPath.toFile()
                botLoggerSupplier = { logger.asMiraiLogger() }
                networkLoggerSupplier  = { logger.asMiraiLogger() }
            }.alsoLogin()
            bot.eventChannel.subscribeAlways<Event> {
                eventManager.post(MiraiBoxEvent(this))
            }
        }
    }
}