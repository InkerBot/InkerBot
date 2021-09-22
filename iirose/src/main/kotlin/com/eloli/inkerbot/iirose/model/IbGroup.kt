package com.eloli.inkerbot.iirose.model

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.model.Receiver
import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.util.Identity
import com.eloli.inkerbot.api.util.ResourceKey
import com.eloli.inkerbot.iirose.event.IbSendRoomMessage
import com.eloli.inkerbot.iirose.util.message.IbTranslator

class IbGroup : Receiver {
    override val identity: Identity = Identity.of("current")
    override val key: ResourceKey = ResourceKey.of("iirose", "room")
    override val name: String = "current"

    override fun sendMessage(message: MessageComponent) {
        InkerBot.eventManager.post(IbSendRoomMessage(IbTranslator.toIb(message), "ffffff"))
    }
}