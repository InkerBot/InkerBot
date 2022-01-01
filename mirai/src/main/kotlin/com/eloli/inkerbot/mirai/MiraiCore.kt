package com.eloli.inkerbot.mirai

import bot.inker.api.InkerBot
import bot.inker.api.event.AutoComponent
import bot.inker.api.event.EventHandler
import bot.inker.api.event.EventManager
import bot.inker.api.event.Order
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.model.Group
import bot.inker.api.model.Member
import bot.inker.api.model.message.AtComponent
import bot.inker.api.model.message.MuiltComponent
import bot.inker.api.plugin.JvmPlugin
import bot.inker.api.plugin.PluginContainer
import bot.inker.api.registry.Registrar
import bot.inker.api.registry.Registries
import bot.inker.api.registry.UpdatableRegistrar
import bot.inker.api.service.CommandService
import com.eloli.inkerbot.mirai.config.MiraiConfig
import com.eloli.inkerbot.mirai.config.MiraiConfigProvider
import com.eloli.inkerbot.mirai.event.MiraiBoxEvent
import com.eloli.inkerbot.mirai.event.MiraiMessageEvent
import com.eloli.inkerbot.mirai.model.MiraiGroup
import com.eloli.inkerbot.mirai.model.MiraiMember
import com.google.inject.Binder
import com.google.inject.Inject
import com.google.inject.TypeLiteral
import com.google.inject.name.Names
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.utils.LoggerAdapters.asMiraiLogger
import okhttp3.internal.indexOfNonWhitespace
import org.slf4j.Logger
import javax.inject.Singleton

@Singleton
@AutoComponent
class MiraiCore : JvmPlugin {
  override fun configure(binder: Binder) {
    binder.bind(MiraiConfig::class.java).toProvider(MiraiConfigProvider::class.java)
  }
  lateinit var bot:Bot

  @Inject
  private lateinit var config: MiraiConfig

  @Inject
  private lateinit var eventManager: EventManager

  @Inject
  private lateinit var plugin: PluginContainer

  @Inject
  private lateinit var logger: Logger

  @EventHandler
  fun onEnable(e: LifecycleEvent.ServerStarted) {
    connect()
  }
  @EventHandler
  fun onDisable(e: LifecycleEvent.ServerStopped) {
    bot.close()
  }

  fun close(){
    bot.close()
  }
  fun connect(){
    runBlocking {
      bot = BotFactory.newBot(config.qqNumber.toLong(), config.qqPassword) {
        fileBasedDeviceInfo(plugin.dataPath.resolve("device.json").toString())
        cacheDir = plugin.dataPath.toFile()
        botLoggerSupplier = { logger.asMiraiLogger() }
        networkLoggerSupplier = { logger.asMiraiLogger() }
      }.alsoLogin()
      // MiraiHandler.register(bot)
      bot.eventChannel.subscribeAlways<Event> {it->
        logger.debug("onMiraiEvent: {}", it)
        eventManager.post(MiraiBoxEvent(it))
      }
    }
  }

  @EventHandler
  fun registerEntity(e: LifecycleEvent.RegisterEntity){
    e.register(MiraiMember.Record::class.java)
    e.register(MiraiGroup.Record::class.java)
  }

  @EventHandler
  fun registerServices(e: LifecycleEvent.RegisterService){
    e.register {
      bind(object : TypeLiteral<Registrar<Member>>() {}).annotatedWith(Names.named(MiraiMember.KEY.toString()))
        .toInstance(UpdatableRegistrar.of(
          Registries.member,
          MiraiMember.KEY,
          Member::class.java,
          MiraiMember::class.java,
          MiraiMember.Record::class.java)
        )
      bind(object : TypeLiteral<Registrar<Group>>() {}).annotatedWith(Names.named(MiraiGroup.KEY.toString()))
        .toInstance(UpdatableRegistrar.of(
          Registries.group,
          MiraiGroup.KEY,
          Group::class.java,
          MiraiGroup::class.java,
          MiraiGroup.Record::class.java)
        )
    }
  }

  @EventHandler(order = Order.POST)
  fun onMessage(event: MiraiMessageEvent) {
    val message = event.message
    if(message !is MuiltComponent){
      return
    }
    val firstComponent = message.subs.firstOrNull()
    if(firstComponent !is AtComponent){
      return
    }
    val firstTarget = firstComponent.target
    if(firstTarget !is MiraiMember){
      return
    }
    if (firstTarget.qqNumber != config.qqNumber) {
      return
    }
    var command = MuiltComponent.of(message.subs.subList(1,message.subs.size)).toString()
    command = command.substring(command.indexOfNonWhitespace())
    InkerBot(CommandService::class).execute(event, command)
  }
}