package bot.inker.core.commands

import bot.inker.api.command.permission
import bot.inker.api.event.AutoComponent
import bot.inker.api.event.EventHandler
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.event.message.MessageEvent
import bot.inker.core.service.InkCommandService
import com.eloli.inkcmd.builder.LiteralArgumentBuilder
import com.eloli.inkcmd.values.IntegerValueType
import org.jline.reader.UserInterruptException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@AutoComponent
class HelpCommand {
    @Inject
    private lateinit var commandService: InkCommandService
    @EventHandler
    fun onRegisterCommand(e: LifecycleEvent.RegisterCommand) {
        e.register("help"){
            describe = "Get list of commands."
            permission("inkerbot.command.help")
            executes {
                commandService.getHelp(it)
            }
            argument("page", IntegerValueType.integer(1)){
                describe = "The page of list"
                executes{
                    commandService.getHelp(it)
                }
            }
        }
    }
}