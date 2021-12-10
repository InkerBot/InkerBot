package bot.inker.core.command

import bot.inker.api.InkerBot
import bot.inker.api.event.message.MessageEvent
import bot.inker.api.model.message.PlainTextComponent
import bot.inker.api.service.CommandService
import bot.inker.api.service.HelpCommand
import com.eloli.inkcmd.context.CommandContext
import java.util.stream.Collectors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InkHelpCommand:HelpCommand {
  @Inject
  lateinit var commandService: CommandService
  override fun run(ctx: CommandContext<MessageEvent>): Int {
    ctx.source.sendMessage(PlainTextComponent.of(
      commandService.dispatcher.getSmartHelp(ctx)
    ))
    return 1
  }
}