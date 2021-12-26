package bot.inker.api.event.message

import bot.inker.api.event.Cancellable
import com.eloli.inkcmd.ParseResults

interface CommandExecuteEvent:Cancellable {
  val messageEvent:MessageEvent
  val parseResults:ParseResults<MessageEvent>
}