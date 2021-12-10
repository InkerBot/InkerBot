package bot.inker.iirose.event

import bot.inker.api.event.Event
import bot.inker.api.event.EventContext
import bot.inker.iirose.util.CodeUtil

class IbRawMessageEvent(val message: String) : Event {
  override val context: EventContext = EventContext.empty()
  val firstChar: String = message.substring(0, 1)
  val split: List<String> = CodeUtil.decode(message.split(">"))

  override fun toString(): String {
    return "IbRawMessageEvent " + message
  }
}