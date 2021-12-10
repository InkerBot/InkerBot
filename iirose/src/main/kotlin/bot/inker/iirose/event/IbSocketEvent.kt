package bot.inker.iirose.event

import bot.inker.api.event.Event
import bot.inker.api.event.EventContext

abstract class IbSocketEvent : Event {
  override val context: EventContext = EventContext.empty()

  class Closed(val code: Int, val reason: String) : IbSocketEvent()
  class Closing(val code: Int, val reason: String) : IbSocketEvent()
  class Open : IbSocketEvent()
  class Failure(val t: Throwable) : IbSocketEvent()
  class Message(val message: String) : IbSocketEvent()
}