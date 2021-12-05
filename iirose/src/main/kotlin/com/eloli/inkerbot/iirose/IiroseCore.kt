package com.eloli.inkerbot.iirose

import com.eloli.inkcmd.builder.LiteralArgumentBuilder
import com.eloli.inkcmd.tree.OptionalNode
import com.eloli.inkcmd.values.BoolValueType
import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.event.EventHandler
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.event.lifestyle.LifecycleEvent
import com.eloli.inkerbot.api.event.message.MessageEvent
import com.eloli.inkerbot.api.model.Group
import com.eloli.inkerbot.api.model.Member
import com.eloli.inkerbot.api.model.message.PlainTextComponent
import com.eloli.inkerbot.api.plugin.JvmPlugin
import com.eloli.inkerbot.api.plugin.PluginContainer
import com.eloli.inkerbot.api.registry.Registrar
import com.eloli.inkerbot.api.registry.UpdatableRegistrar
import com.eloli.inkerbot.api.service.CommandService
import com.eloli.inkerbot.api.service.DatabaseService
import com.eloli.inkerbot.iirose.config.IbConfig
import com.eloli.inkerbot.iirose.config.IbConfigProvider
import com.eloli.inkerbot.iirose.event.IbGroupMessageEvent
import com.eloli.inkerbot.iirose.event.IbPrivateMessageEvent
import com.eloli.inkerbot.iirose.event.IbSendPrivateMessage
import com.eloli.inkerbot.iirose.event.IbSendRoomMessage
import com.eloli.inkerbot.iirose.model.IbGroup
import com.eloli.inkerbot.iirose.model.IbMember
import com.google.inject.Binder
import com.google.inject.TypeLiteral
import com.google.inject.name.Names
import java.lang.StringBuilder
import java.util.stream.Collectors
import javax.inject.Inject

class IiroseCore : JvmPlugin {
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

  @Inject
  private lateinit var commandService: CommandService

  @EventHandler
  fun onBoot(event: LifecycleEvent.Enable) {
    connection.onBoot()
    eventManager.registerListeners(
      pluginContainer,
      InkerBot.injector.getInstance(IbGroupMessageEvent.Resolver::class.java)
    )
    eventManager.registerListeners(
      pluginContainer,
      InkerBot.injector.getInstance(IbPrivateMessageEvent.Resolver::class.java)
    )
    eventManager.registerListeners(
      pluginContainer,
      InkerBot.injector.getInstance(IbSendRoomMessage.Resolver::class.java)
    )
    eventManager.registerListeners(
      pluginContainer,
      InkerBot.injector.getInstance(IbSendPrivateMessage.Resolver::class.java)
    )
  }

  @EventHandler
  fun onRegisterService(event: LifecycleEvent.RegisterService) {
    event.binder.bind(object : TypeLiteral<Registrar<Member>>() {}).annotatedWith(Names.named("iirose:member"))
      .toInstance(UpdatableRegistrar.of(Member::class.java, IbMember::class.java, IbMember.Record::class.java))
    event.binder.bind(object : TypeLiteral<Registrar<Group>>() {}).annotatedWith(Names.named("iirose:room"))
      .toInstance(UpdatableRegistrar.of(Group::class.java, IbGroup::class.java, IbGroup.Record::class.java))
  }

  @EventHandler
  fun onRegisterEntity(event: LifecycleEvent.RegisterEntity) {
    event.register(IbGroup.Record::class.java)
    event.register(IbMember.Record::class.java)
  }

  @EventHandler
  fun onRegisterCommand(event: LifecycleEvent.RegisterCommand) {
    event.register(
      LiteralArgumentBuilder.literal<MessageEvent>("iirose")
        .setDescribe("IIROSE BOT MANAGER")
        .then(
          LiteralArgumentBuilder.literal<MessageEvent>("username")
            .withOption(
              OptionalNode.builder<MessageEvent>()
                .name("help")
                .type(BoolValueType.bool())
                .defaultValue(false)
                .defineValue(true).build()
            )
            .setDescribe("Get IIROSE's username.")
            .executes {
              if(it.getOption("help",Boolean::class.java)){
                val builder = StringBuilder()
                builder.appendLine("+++ Help text +++")
                val smartUsage =  commandService.dispatcher.getSmartUsage(it.nodes.last().node,it.source)
                val prefix = it.nodes.stream()
                  .map { it.node.usageText }
                  .collect(Collectors.joining(" "))
                if(smartUsage.isNotEmpty()){
                  smartUsage.forEach { k, v ->
                    builder.append(prefix).append(v).append(" : ").appendLine(k.describe)
                  }
                }
                val optionBuilder = StringBuilder()
                for(node in it.nodes){
                  for (option in node.node.options.values) {
                    optionBuilder.append("--").append(option.name).append(" : ").appendLine(option.describe)
                  }
                }
                it.source.sendMessage(PlainTextComponent.of(builder.toString()))
                return@executes 1
              }
              it.source.sendMessage(
                PlainTextComponent.of(
                  InkerBot(IbConfig::class).username
                )
              )
              1
            }
            .build()
        )
        .build()
    )
  }

  @EventHandler
  fun onMessage(event: MessageEvent) {
    val prefix = " [*${InkerBot(IbConfig::class).username}*] ";
    if (event.message.toString().startsWith(prefix)) {
      InkerBot(CommandService::class).execute(event.message.toString().substring(prefix.length), event)
    }
  }
}