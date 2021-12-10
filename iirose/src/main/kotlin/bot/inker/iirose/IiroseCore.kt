package bot.inker.iirose

import bot.inker.api.InkerBot
import bot.inker.api.event.EventHandler
import bot.inker.api.event.EventManager
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.event.message.MessageEvent
import bot.inker.api.model.Group
import bot.inker.api.model.Member
import bot.inker.api.model.message.PlainTextComponent
import bot.inker.api.plugin.JvmPlugin
import bot.inker.api.plugin.PluginContainer
import bot.inker.api.registry.Registrar
import bot.inker.api.registry.UpdatableRegistrar
import bot.inker.api.service.CommandService
import bot.inker.api.service.DatabaseService
import bot.inker.iirose.config.IbConfig
import bot.inker.iirose.config.IbConfigProvider
import bot.inker.iirose.event.IbGroupMessageEvent
import bot.inker.iirose.event.IbPrivateMessageEvent
import bot.inker.iirose.event.IbSendPrivateMessage
import bot.inker.iirose.event.IbSendRoomMessage
import bot.inker.iirose.model.IbGroup
import bot.inker.iirose.model.IbMember
import com.eloli.inkcmd.builder.LiteralArgumentBuilder
import com.eloli.inkcmd.builder.ValuedArgumentBuilder
import com.eloli.inkcmd.values.StringValueType
import com.google.inject.Binder
import com.google.inject.TypeLiteral
import com.google.inject.name.Names
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
      bot.inker.api.InkerBot.injector.getInstance(IbGroupMessageEvent.Resolver::class.java)
    )
    eventManager.registerListeners(
      pluginContainer,
      bot.inker.api.InkerBot.injector.getInstance(IbPrivateMessageEvent.Resolver::class.java)
    )
    eventManager.registerListeners(
      pluginContainer,
      bot.inker.api.InkerBot.injector.getInstance(IbSendRoomMessage.Resolver::class.java)
    )
    eventManager.registerListeners(
      pluginContainer,
      bot.inker.api.InkerBot.injector.getInstance(IbSendPrivateMessage.Resolver::class.java)
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
  fun onMessage(event: MessageEvent) {
    val prefix = " [*${bot.inker.api.InkerBot(IbConfig::class).username}*] ";
    if (event.message.toString().startsWith(prefix)) {
      bot.inker.api.InkerBot(CommandService::class).execute(event.message.toString().substring(prefix.length), event)
    }
  }

  @EventHandler
  fun onRegisterCommand(event: LifecycleEvent.RegisterCommand) {
    event.register(
      LiteralArgumentBuilder.literal<MessageEvent>("iirose")
        .describe("IIROSE provider for InkerBot.")
        .then(
          LiteralArgumentBuilder.literal<MessageEvent>("config")
            .describe("配置iirose")
            .then(
              LiteralArgumentBuilder.literal<MessageEvent>("username")
                .describe("配置iirose所用的用户名")
                .executes {
                  it.source.sendMessage(
                    PlainTextComponent.of("目前配置的用户名为：${InkerBot(IbConfig::class).username}")
                  )
                  1
                }
                .then(
                  ValuedArgumentBuilder.argument<MessageEvent?, String?>("newname", StringValueType.string())
                    .describe("配置iirose所用的用户名")
                    .executes {
                      InkerBot(IbConfig::class).username = it.getArgument("newname", String::class.java)
                      InkerBot(IbConfigProvider::class.java).save()
                      it.source.sendMessage(
                        PlainTextComponent.of("配置完成，使用 \"/iirose login\" 生效")
                      )
                      1
                    }.build()
                ).build()
            )
            .then(
              LiteralArgumentBuilder.literal<MessageEvent>("password")
                .describe("配置iirose所用的密码")
                .then(
                  ValuedArgumentBuilder.argument<MessageEvent?, String?>("newpassword", StringValueType.string())
                    .describe("配置iirose所用的密码")
                    .executes {
                      bot.inker.api.InkerBot(IbConfig::class).password =
                        it.getArgument("newpassword", String::class.java)
                      InkerBot(IbConfigProvider::class.java).save()
                      it.source.sendMessage(
                        PlainTextComponent.of("配置完成，使用 \"/iirose login\" 生效")
                      )
                      1
                    }.build()
                ).build()
            )
            .then(
              LiteralArgumentBuilder.literal<MessageEvent>("room")
                .describe("配置iirose的房间")
                .then(
                  ValuedArgumentBuilder.argument<MessageEvent?, String?>("newroom", StringValueType.string())
                    .describe("配置iirose的房间")
                    .executes {
                      InkerBot(IbConfig::class).room = it.getArgument("newroom", String::class.java)
                      InkerBot(IbConfigProvider::class.java).save()
                      it.source.sendMessage(
                        PlainTextComponent.of("配置完成，使用 \"/iirose login\" 生效")
                      )
                      1
                    }.build()
                ).build()
            ).build()
        )
        .then(
          LiteralArgumentBuilder.literal<MessageEvent>("login")
            .describe("重新登录IIROSE")
            .executes {
              it.source.sendMessage(
                PlainTextComponent.of("正在重新登录")
              )
              InkerBot(IbConnection::class).apply {
                close()
                connect()
              }
              1
            }.build()
        ).build()
    )
  }
}