package com.eloli.inkerbot.api.service

import com.eloli.inkcmd.CommandDispatcher
import com.eloli.inkerbot.api.event.message.MessageEvent

interface CommandService {
  val dispatcher: CommandDispatcher<MessageEvent>
  fun execute(line: String, source: MessageEvent)
}