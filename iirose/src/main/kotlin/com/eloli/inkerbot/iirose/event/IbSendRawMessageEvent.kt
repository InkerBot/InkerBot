package com.eloli.inkerbot.iirose.event

import com.eloli.inkerbot.api.event.Event
import com.eloli.inkerbot.api.event.EventContext
import com.eloli.inkerbot.iirose.util.CodeUtil

class IbSendRawMessageEvent(val message:String):Event{
    override val context: EventContext = EventContext.empty()
    override fun toString(): String {
        return "IbSendRawMessageEvent(message='$message', context=$context)"
    }

}