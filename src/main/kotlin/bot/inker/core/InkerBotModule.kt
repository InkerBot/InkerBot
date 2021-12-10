package bot.inker.core

import bot.inker.api.Frame
import bot.inker.api.InkerBot
import bot.inker.api.ServiceManager
import bot.inker.api.config.ConfigService
import bot.inker.api.event.EventContextKey
import bot.inker.api.event.EventHandler
import bot.inker.api.event.EventManager
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.event.message.MessageEvent
import bot.inker.api.model.ConsoleSender
import bot.inker.api.model.Member
import bot.inker.api.model.message.AtComponent
import bot.inker.api.model.message.MuiltComponent
import bot.inker.api.model.message.PlainTextComponent
import bot.inker.api.plugin.*
import bot.inker.api.registry.Registrar
import bot.inker.api.registry.Registry
import bot.inker.api.registry.UpdatableRegistrar
import bot.inker.api.service.CommandService
import bot.inker.api.service.DatabaseService
import bot.inker.api.service.HelpCommand
import bot.inker.api.util.Identity
import bot.inker.api.util.ResourceKey
import bot.inker.core.command.InkCommandService
import bot.inker.core.command.InkHelpCommand
import bot.inker.core.config.InkConfigService
import bot.inker.core.event.InkEventContextKey
import bot.inker.core.event.InkEventManager
import bot.inker.core.model.InkAt
import bot.inker.core.model.InkConsoleSender
import bot.inker.core.model.InkMuilt
import bot.inker.core.model.InkPlainText
import bot.inker.core.plugin.InkPluginDepend
import bot.inker.core.plugin.InkPluginManager
import bot.inker.core.plugin.InkPluginMeta
import bot.inker.core.plugin.InkPluginUrls
import bot.inker.core.registry.InkConsoleMemberRegistry
import bot.inker.core.registry.InkRegistrar
import bot.inker.core.registry.InkRegistry
import bot.inker.core.service.H2DatabaseService
import bot.inker.core.service.InkDatabaseService
import bot.inker.core.setting.ImplSettingProvider
import bot.inker.core.setting.InkSetting
import bot.inker.core.util.InkIdentity
import bot.inker.core.util.InkResourceKey
import com.eloli.inkcmd.builder.LiteralArgumentBuilder
import com.eloli.inkcmd.builder.ValuedArgumentBuilder
import com.eloli.inkcmd.values.StringValueType
import com.google.inject.Binder
import com.google.inject.TypeLiteral
import com.google.inject.name.Names
import org.slf4j.Logger

class InkerBotModule : JvmPlugin {
  override fun configure(binder: Binder) {
    binder.bind(EventContextKey.Builder::class.java).to(InkEventContextKey.Builder::class.java)
    binder.bind(EventManager::class.java).to(InkEventManager::class.java)

    binder.bind(ConsoleSender::class.java).to(InkConsoleSender::class.java)
    binder.bind(AtComponent.Factory::class.java).to(InkAt.Factory::class.java)
    binder.bind(MuiltComponent.Factory::class.java).to(InkMuilt.Factory::class.java)
    binder.bind(PlainTextComponent.Factory::class.java).to(InkPlainText.Factory::class.java)
    binder.bind(MuiltComponent.Builder::class.java).to(InkMuilt.Builder::class.java)

    binder.bind(PluginDepend.Builder::class.java).to(InkPluginDepend.Builder::class.java)
    binder.bind(PluginManager::class.java).to(InkPluginManager::class.java)
    binder.bind(PluginMeta.Builder::class.java).to(InkPluginMeta.Builder::class.java)
    binder.bind(PluginUrls.Builder::class.java).to(InkPluginUrls.Builder::class.java)

    binder.bind(ResourceKey.Factory::class.java).to(InkResourceKey.Factory::class.java)
    binder.bind(Identity.Factory::class.java).to(InkIdentity.Factory::class.java)

    binder.bind(Registry.Factory::class.java).to(InkRegistry.Factory::class.java)
    binder.bind(UpdatableRegistrar.Factory::class.java).to(InkRegistrar.Factory::class.java)

    binder.bind(ConfigService.Factory::class.java).to(InkConfigService.Factory::class.java)
    binder.bind(InkSetting::class.java).toProvider(ImplSettingProvider::class.java)

    binder.bind(bot.inker.api.Frame::class.java).to(bot.inker.core.InkFrame::class.java)
    binder.bind(ServiceManager::class.java).to(InkServiceManager::class.java)
    binder.bind(DatabaseService::class.java).to(InkDatabaseService::class.java)
    binder.bind(HelpCommand::class.java).to(InkHelpCommand::class.java)
    binder.bind(CommandService::class.java).to(InkCommandService::class.java)
    // binder.bind(Session::class.java).to(InkDatabaseService::class.java)
  }

  @EventHandler
  fun onRegisterService(e: LifecycleEvent.RegisterService) {
    e.binder.bind(DatabaseService::class.java).annotatedWith(Names.named("h2")).to(H2DatabaseService::class.java)

    e.binder.bind(object : TypeLiteral<Registrar<Member>>() {}).annotatedWith(Names.named("inkerbot:console"))
      .to(InkConsoleMemberRegistry::class.java)
  }

  @EventHandler
  fun onRegisterCommand(e: LifecycleEvent.RegisterCommand) {
    e.register(LiteralArgumentBuilder.literal<MessageEvent>("log").describe("Just log it").then(
      ValuedArgumentBuilder.argument<MessageEvent?, String?>("content",StringValueType.greedyString())
        .describe("content")
        .executes {
          InkerBot(Frame::class).logger.error(it.getArgument("content", String::class.java))
          1
        }
    ).build())
  }
}