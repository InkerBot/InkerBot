package com.eloli.inkerbot.mirai

import com.eloli.inkerbot.api.event.EventContext

class MiraiBoxEvent(val mirai: net.mamoe.mirai.event.Event) : com.eloli.inkerbot.api.event.Event {
  override val context: EventContext = EventContext.empty()
}