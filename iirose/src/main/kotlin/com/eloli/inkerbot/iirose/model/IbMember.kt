package com.eloli.inkerbot.iirose.model

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.model.Group
import com.eloli.inkerbot.api.model.Member
import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.registry.Registries
import com.eloli.inkerbot.api.util.Identity
import com.eloli.inkerbot.api.util.ResourceKey
import com.eloli.inkerbot.iirose.event.IbSendPrivateMessage
import com.eloli.inkerbot.iirose.registry.IbMemberRegistrar
import com.eloli.inkerbot.iirose.util.message.IbTranslator
import javax.inject.Singleton

class IbMember(
    val userId:String,
    override var name: String
) :Member {
    override val identity: Identity = Identity.Companion.of(userId)
    override val key: ResourceKey= KEY
    override fun sendMessage(message: MessageComponent) {
        InkerBot.eventManager.post(IbSendPrivateMessage(userId, IbTranslator.toIb(message),"ffffff"))
    }

    override fun toString(): String {
        return "IbMember(userId='$userId', name='$name', identity=$identity, key=$key)"
    }


    companion object{
        val KEY = ResourceKey.of("iirose","member")
        fun factory():Factory{
            return InkerBot.injector.getInstance(Factory::class.java)
        }
        fun of(userId:String, name: String):IbMember{
            return factory().of(userId, name)
        }
    }

    @Singleton
    class Factory{
        private val registrar:IbMemberRegistrar = Registries.member.get(KEY) as IbMemberRegistrar
        fun of(userId:String, name: String):IbMember{
            return registrar.set(userId, name)
        }
    }
}