package bot.inker.iirose

import bot.inker.api.InkerBot
import bot.inker.api.event.AutoComponent
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
import bot.inker.api.registry.Registries
import bot.inker.api.registry.UpdatableRegistrar
import bot.inker.api.service.CommandService
import bot.inker.api.service.DatabaseService
import bot.inker.iirose.config.IbConfig
import bot.inker.iirose.config.IbConfigProvider
import bot.inker.iirose.event.IbGroupMessageEvent
import bot.inker.iirose.event.IbMessageEvent
import bot.inker.iirose.model.IbGroup
import bot.inker.iirose.model.IbMember
import com.eloli.inkcmd.values.BoolValueType
import com.eloli.inkcmd.values.StringValueType
import com.google.inject.Binder
import com.google.inject.TypeLiteral
import com.google.inject.name.Names
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@AutoComponent
class IiroseCore : JvmPlugin {
  override fun configure(binder: Binder) {
    binder.bind(IbConfig::class.java).toProvider(IbConfigProvider::class.java)
  }

  @EventHandler
  fun onRegisterService(event: LifecycleEvent.RegisterService) {
    event.register {
      bind(object : TypeLiteral<Registrar<Member>>() {}).annotatedWith(Names.named("iirose:member"))
        .toInstance(UpdatableRegistrar.of(
          Registries.member,
          IbMember.KEY,
          Member::class.java,
          IbMember::class.java,
          IbMember.Record::class.java
        ))
      bind(object : TypeLiteral<Registrar<Group>>() {}).annotatedWith(Names.named("iirose:room"))
        .toInstance(UpdatableRegistrar.of(
          Registries.group,
          IbGroup.KEY,
          Group::class.java,
          IbGroup::class.java,
          IbGroup.Record::class.java
        ))
    }
  }

  @EventHandler
  fun onRegisterEntity(event: LifecycleEvent.RegisterEntity) {
    event.register(IbGroup.Record::class.java)
    event.register(IbMember.Record::class.java)
  }

  @EventHandler
  fun onMessage(event: IbMessageEvent) {
    val prefix = " [*${InkerBot(IbConfig::class).username}*] ";
    if (event.message.toString().startsWith(prefix)) {
      InkerBot(CommandService::class).execute(event.message.toString().substring(prefix.length), event)
    }
  }

  @EventHandler
  fun onRegisterCommand(event: LifecycleEvent.RegisterCommand) {
    event.register("iirose"){
      describe = "为 InkerBot IIROSE 插件根命令。"
      option("login-now",BoolValueType.bool()){
        describe("在配置完成后立即应用")
        defaultValue(false)
        defineValue(true)
      }
      literal("config"){
        describe = "配置 IIROSE"
        literal("username"){
          describe = "获取 IIROSE 当前的用户名"
          executes {
            it.source.sendMessage(
              PlainTextComponent.of("目前配置的用户名为：${InkerBot(IbConfig::class).username}")
            )
          }
          argument("newname",StringValueType.string()){
            describe = "配置 IIROSE 所用的用户名"
            executes {
              InkerBot(IbConfig::class).username = it.getArgument("newname", String::class.java)
              InkerBot(IbConfigProvider::class.java).save()
              if(it.getOption("login-now",Boolean::class.java)){
                it.source.sendMessage(
                  PlainTextComponent.of("配置完成，立即生效")
                )
                InkerBot(IbConnection::class).apply {
                  close()
                  connect()
                }
              }else{
                it.source.sendMessage(
                  PlainTextComponent.of("配置完成，使用 \"/iirose login\" 生效")
                )
              }
            }
          }
        }
        literal("password"){
          describe = "配置 IIROSE 所用的密码"
          argument("newpassword",StringValueType.string()){
            describe = "配置 IIROSE 所用的密码"
            executes {
              InkerBot(IbConfig::class).password = it.getArgument("newpassword", String::class.java)
              InkerBot(IbConfigProvider::class.java).save()
              if(it.getOption("login-now",Boolean::class.java)){
                it.source.sendMessage(
                  PlainTextComponent.of("配置完成，立即生效")
                )
                InkerBot(IbConnection::class).apply {
                  close()
                  connect()
                }
              }else{
                it.source.sendMessage(
                  PlainTextComponent.of("配置完成，使用 \"/iirose login\" 生效")
                )
              }
            }
          }
        }
        literal("room"){
          describe = "获取 IIROSE 当前的房间"
          executes {
            it.source.sendMessage(
              PlainTextComponent.of("目前配置的房间为：${InkerBot(IbConfig::class).room}")
            )
          }
          argument("newroom",StringValueType.string()){
            describe = "配置 IIROSE 所用的房间"
            executes {
              InkerBot(IbConfig::class).room = it.getArgument("newroom", String::class.java)
              InkerBot(IbConfigProvider::class.java).save()
              if(it.getOption("login-now",Boolean::class.java)){
                it.source.sendMessage(
                  PlainTextComponent.of("配置完成，立即生效")
                )
                InkerBot(IbConnection::class).apply {
                  close()
                  connect()
                }
              }else{
                it.source.sendMessage(
                  PlainTextComponent.of("配置完成，使用 \"/iirose login\" 生效")
                )
              }
            }
          }
        }
      }
      literal("login"){
        describe = "重新登录IIROSE"
        executes {
          it.source.sendMessage(
            PlainTextComponent.of("正在重新登录")
          )
          InkerBot(IbConnection::class).apply {
            close()
            connect()
          }
        }
      }
      literal("status"){
        describe = "获取 IIROSE 机器人的一些状态"
        executes {
          val builder = StringBuilder()
          val session = InkerBot(DatabaseService::class).session
          val hql = "SELECT COUNT(*) FROM ib_member_record"
          val countAll = session.createQuery(hql).list()[0]
          builder.appendLine("+++ IIROSE 状态面板 +++")
          builder.appendLine("已存储 $countAll 条用户信息")
          it.source.sendMessage(
            PlainTextComponent.of(builder.toString())
          )
        }
      }
    }
  }
}