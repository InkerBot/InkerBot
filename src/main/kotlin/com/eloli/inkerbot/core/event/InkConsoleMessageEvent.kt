package com.eloli.inkerbot.core.event

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.event.EventContext
import com.eloli.inkerbot.api.event.message.ConsoleMessageEvent
import com.eloli.inkerbot.api.model.ConsoleSender
import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.model.message.PlainTextComponent

class InkConsoleMessageEvent(
  val content: String
) : ConsoleMessageEvent {
  override val message: MessageComponent = PlainTextComponent.of(content)
  override val sender: ConsoleSender = InkerBot()

  override fun sendMessage(message: MessageComponent) {
    sender.sendMessage(message)
  }

  override val context: EventContext = EventContext.empty()

}