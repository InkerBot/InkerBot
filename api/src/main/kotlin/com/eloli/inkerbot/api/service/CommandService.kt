package com.eloli.inkerbot.api.service

import com.eloli.inkcmd.Command
import com.eloli.inkcmd.CommandDispatcher
import com.eloli.inkcmd.builder.ArgumentBuilder
import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.event.message.MessageEvent

interface CommandService {
  val dispatcher: CommandDispatcher<MessageEvent>
  fun execute(line: String, source: MessageEvent)
  fun <T: ArgumentBuilder<MessageEvent, T>> withSmartHelpOption(argument: ArgumentBuilder<MessageEvent, T>):T
  fun <T:ArgumentBuilder<MessageEvent,T>> withSmartHelpCommand(argument:ArgumentBuilder<MessageEvent,T>, command: Command<MessageEvent>):T
}

fun <T: ArgumentBuilder<MessageEvent, T>> ArgumentBuilder<MessageEvent, T>.withSmartHelpOption():T{
  return InkerBot(CommandService::class).withSmartHelpOption(this)
}
fun <T:ArgumentBuilder<MessageEvent,T>> ArgumentBuilder<MessageEvent, T>.withSmartHelpCommand(command: Command<MessageEvent>):T{
  return InkerBot(CommandService::class).withSmartHelpCommand(this,command)
}
