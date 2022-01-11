package bot.inker.core.commands

import bot.inker.api.InkerBot
import bot.inker.api.event.AutoComponent
import bot.inker.api.event.EventHandler
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.event.message.MessageEvent
import bot.inker.core.service.InkCommandService
import com.eloli.inkcmd.builder.LiteralArgumentBuilder
import org.jline.reader.LineReader
import org.jline.reader.UserInterruptException
import javax.inject.Singleton

@Singleton
@AutoComponent
class StopCommand {
    @EventHandler
    fun onRegisterCommand(e: LifecycleEvent.RegisterCommand) {
        e.register(
            LiteralArgumentBuilder.literal<MessageEvent>("stop")
                .describe("Stop InkerBot")
                .executes {
                    throw UserInterruptException("stop")
                }
                .build()
        )
    }
}