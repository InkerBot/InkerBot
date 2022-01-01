package bot.inker.core.event

import bot.inker.api.event.EventContext
import bot.inker.api.event.message.MessageEvent
import bot.inker.api.model.Member
import bot.inker.api.model.message.MessageComponent
import bot.inker.api.model.message.PlainTextComponent

class InkPerformEvent(
  override val sender: Member,
  val command: String
) :MessageEvent{
  override val message: MessageComponent = PlainTextComponent.of("/$command")

  override fun sendMessage(message: MessageComponent) {
    sender.sendMessage(message)
  }

  override val context: EventContext = EventContext.empty()
  override var cancelled: Boolean = false
}