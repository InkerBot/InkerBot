package bot.inker.iirose.event

import bot.inker.api.event.Event
import bot.inker.api.event.EventContext

class IbSendRawMessageEvent(val message: String) : Event {
  override val context: EventContext = EventContext.empty()
  override fun toString(): String {
    return "IbSendRawMessageEvent(message='$message', context=$context)"
  }

}