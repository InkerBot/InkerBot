package com.eloli.inkerbot.iirose.event

import com.eloli.inkerbot.api.event.Event
import com.eloli.inkerbot.api.event.EventContext
import com.eloli.inkerbot.iirose.util.CodeUtil

class IbRawMessageEvent(val message: String) : Event {
    override val context: EventContext = EventContext.empty()
    val firstChar: String = message.substring(0, 1)
    val split: List<String> = CodeUtil.decode(message.split(">"))
}