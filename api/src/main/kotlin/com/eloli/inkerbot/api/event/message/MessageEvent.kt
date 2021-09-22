package com.eloli.inkerbot.api.event.message

import com.eloli.inkerbot.api.event.Event
import com.eloli.inkerbot.api.model.Receiver
import com.eloli.inkerbot.api.model.Sender
import com.eloli.inkerbot.api.model.message.MessageComponent

interface MessageEvent : Event {
    val sender: Sender
    val reply: Receiver
    val message: MessageComponent
}