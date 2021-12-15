package bot.inker.core.event.lifestyle

import bot.inker.api.event.EventContext
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.event.message.MessageEvent
import com.eloli.inkcmd.tree.CommandNode
import com.google.inject.Binder
import javax.inject.Singleton

@Singleton
abstract class InkLifecycleEvent : LifecycleEvent {
  override val context: EventContext = EventContext.empty()

  class Enable : InkLifecycleEvent(), LifecycleEvent.Enable {

  }

  class RegisterService(override val binder: Binder) : InkLifecycleEvent(), LifecycleEvent.RegisterService {

  }

  class RegisterEntity(private val action: (Class<*>) -> Unit) : InkLifecycleEvent(), LifecycleEvent.RegisterEntity {
    override fun register(entity: Class<*>) {
      action.invoke(entity)
    }
  }

  class RegisterCommand(private val action: (CommandNode<MessageEvent>) -> Unit) : InkLifecycleEvent(),
    LifecycleEvent.RegisterCommand {
    override fun register(command: CommandNode<MessageEvent>) {
      action.invoke(command)
    }
  }
}