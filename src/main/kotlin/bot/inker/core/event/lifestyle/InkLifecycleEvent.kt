package bot.inker.core.event.lifestyle

import bot.inker.api.event.EventContext
import bot.inker.api.event.lifestyle.LifeCycle
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.event.message.MessageEvent
import com.eloli.inkcmd.builder.LiteralArgumentBuilder
import com.eloli.inkcmd.ktdsl.LiteralArgumentBuilderDsl
import com.eloli.inkcmd.ktdsl.invoke
import com.eloli.inkcmd.tree.CommandNode
import com.google.inject.Binder
import javax.inject.Singleton

abstract class InkLifecycleEvent : LifecycleEvent {
  override val context: EventContext = EventContext.empty()

  class Construction : LifecycleEvent.Construction,BootEvent(LifeCycle.CONSTRUCTION)
  class PreInitialization : LifecycleEvent.PreInitialization,BootEvent(LifeCycle.PRE_INITIALIZATION)
  class Initialization : LifecycleEvent.Initialization,
    BootEvent(LifeCycle.INITIALIZATION),
    LifecycleEvent.RegisterService,
    LifecycleEvent.RegisterEntity,
    LifecycleEvent.RegisterCommand
  {
    lateinit var registerService: (Binder.()->Unit) -> Unit
    lateinit var registerEntity: (Class<*>) -> Unit
    lateinit var registerCommand: (CommandNode<MessageEvent>) -> Unit

    override fun register(binder: Binder.()->Unit) {
      registerService.invoke(binder)
    }

    override fun register(entity: Class<*>) {
      registerEntity.invoke(entity)
    }

    override fun register(command: CommandNode<MessageEvent>) {
      registerCommand.invoke(command)
    }

    override fun register(name: String, action: LiteralArgumentBuilderDsl<MessageEvent>.() -> Unit) {
      register(
        LiteralArgumentBuilder.literal<MessageEvent>(name).invoke(action)
      )
    }
  }
  class PostInitialization : LifecycleEvent.PostInitialization,BootEvent(LifeCycle.POST_INITIALIZATION)
  class LoadComplete : LifecycleEvent.LoadComplete,BootEvent(LifeCycle.LOAD_COMPLETE)
  class ServerAboutToStart : LifecycleEvent.ServerAboutToStart,BootEvent(LifeCycle.SERVER_ABOUT_TO_START)
  class ServerStarting : LifecycleEvent.ServerStarting,BootEvent(LifeCycle.SERVER_STARTING)
  class ServerStarted : LifecycleEvent.ServerStarted,BootEvent(LifeCycle.SERVER_STARTED)
  class ServerStopping : LifecycleEvent.ServerStopping,BootEvent(LifeCycle.SERVER_STOPPING)
  class ServerStopped : LifecycleEvent.ServerStopped,BootEvent(LifeCycle.SERVER_STOPPED)
  class BotStopping : LifecycleEvent.BotStopping,BootEvent(LifeCycle.BOT_STOPPING)
  class BotStopped : LifecycleEvent.BotStopped,BootEvent(LifeCycle.BOT_STOPPED)

  abstract class BootEvent(override val lifeCycle: LifeCycle) : InkLifecycleEvent(),LifecycleEvent.BootEvent
}