package bot.inker.core.event

import bot.inker.api.event.EventContext
import bot.inker.api.event.message.CommandExecuteEvent
import bot.inker.api.event.message.MessageEvent
import com.eloli.inkcmd.ParseResults

class InkCommandExecuteEvent(
  override val messageEvent: MessageEvent,
  override val parseResults: ParseResults<MessageEvent>
) :CommandExecuteEvent {
  override var cancelled: Boolean = false
  override val context: EventContext = EventContext.empty()
}