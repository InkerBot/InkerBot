package bot.inker.api.service

import bot.inker.api.event.message.MessageEvent
import com.eloli.inkcmd.Command
import com.eloli.inkcmd.CommandDispatcher
import com.eloli.inkcmd.ParseResults
import com.eloli.inkcmd.builder.ArgumentBuilder

interface CommandService {
  val dispatcher: CommandDispatcher<MessageEvent>
  fun execute(line: String, source: MessageEvent)
  fun <T : ArgumentBuilder<MessageEvent, T>> withSmartHelpOption(argument: ArgumentBuilder<MessageEvent, T>): T
  fun <T : ArgumentBuilder<MessageEvent, T>> withSmartHelpCommand(
    argument: ArgumentBuilder<MessageEvent, T>,
    command: Command<MessageEvent>
  ): T
}