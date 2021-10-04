package com.eloli.inkerbot.api.event.message

import com.eloli.inkerbot.api.event.Event
import com.eloli.inkerbot.api.model.Member
import com.eloli.inkerbot.api.model.message.MessageComponent

interface MessageEvent:Event {
    val sender: Member
    val message: MessageComponent

    fun sendMessage(message: MessageComponent)
}