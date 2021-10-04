package com.eloli.inkerbot.api.event.message

import com.eloli.inkerbot.api.event.Event
import com.eloli.inkerbot.api.model.Group
import com.eloli.inkerbot.api.model.Member
import com.eloli.inkerbot.api.model.message.MessageComponent

interface GroupMessageEvent:MessageEvent {
    val group: Group
}