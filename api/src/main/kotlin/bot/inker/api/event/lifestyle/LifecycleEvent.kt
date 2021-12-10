package bot.inker.api.event.lifestyle

import bot.inker.api.event.Event
import bot.inker.api.event.message.MessageEvent
import com.eloli.inkcmd.tree.CommandNode
import com.google.inject.Binder

interface LifecycleEvent : Event {
  interface Enable : LifecycleEvent

  interface RegisterService : LifecycleEvent {
    val binder: Binder
  }

  interface RegisterEntity : LifecycleEvent {
    fun register(entity: Class<*>)
  }

  interface RegisterCommand : LifecycleEvent {
    fun register(command: CommandNode<MessageEvent>)
  }
}