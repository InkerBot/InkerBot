package com.eloli.inkerbot.iirose.model

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.model.Receiver
import com.eloli.inkerbot.api.model.Sender
import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.util.Identity
import com.eloli.inkerbot.api.util.ResourceKey
import com.eloli.inkerbot.iirose.event.IbSendPrivateMessage
import com.eloli.inkerbot.iirose.util.message.IbTranslator

class IbMember(val userId:String, override val name: String) :Sender,Receiver {
    override val identity: Identity = Identity.Companion.of(userId)
    override val key: ResourceKey= ResourceKey.of("iirose","member")
    override fun sendMessage(message: MessageComponent) {
        InkerBot.eventManager.post(IbSendPrivateMessage(userId, IbTranslator.toIb(message),"ffffff"))
    }
}