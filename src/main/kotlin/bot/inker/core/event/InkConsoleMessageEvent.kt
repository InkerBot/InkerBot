package bot.inker.core.event

import bot.inker.api.InkerBot
import bot.inker.api.event.EventContext
import bot.inker.api.event.message.ConsoleMessageEvent
import bot.inker.api.model.ConsoleSender
import bot.inker.api.model.message.MessageComponent
import bot.inker.api.model.message.PlainTextComponent

class InkConsoleMessageEvent(
  val content: String
) : ConsoleMessageEvent {
  override var cancelled: Boolean = false
  override val message: MessageComponent = PlainTextComponent.of(content)
  override val sender: ConsoleSender = InkerBot()

  override fun sendMessage(message: MessageComponent) {
    sender.sendMessage(message)
  }

  override val context: EventContext = EventContext.empty()

}