package bot.inker.api.event.message

import bot.inker.api.event.Cancellable
import bot.inker.api.event.Event
import bot.inker.api.model.Member
import bot.inker.api.model.message.MessageComponent

interface MessageEvent : Event,Cancellable {
  val sender: Member
  val message: MessageComponent

  fun sendMessage(message: MessageComponent)
}