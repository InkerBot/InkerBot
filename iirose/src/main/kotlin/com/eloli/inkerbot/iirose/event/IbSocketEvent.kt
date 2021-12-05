package com.eloli.inkerbot.iirose.event

import com.eloli.inkerbot.api.event.Event
import com.eloli.inkerbot.api.event.EventContext

abstract class IbSocketEvent : Event {
  override val context: EventContext = EventContext.empty()

  class Closed(val code: Int, val reason: String) : IbSocketEvent()
  class Closing(val code: Int, val reason: String) : IbSocketEvent()
  class Open : IbSocketEvent()
  class Failure(val t: Throwable) : IbSocketEvent()
  class Message(val message: String) : IbSocketEvent()
}