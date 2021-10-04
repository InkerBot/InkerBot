package com.eloli.inkerbot.iirose.model

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.model.Group
import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.registry.Registries
import com.eloli.inkerbot.api.util.Identity
import com.eloli.inkerbot.api.util.ResourceKey
import com.eloli.inkerbot.iirose.config.IbConfig
import com.eloli.inkerbot.iirose.event.IbSendRoomMessage
import com.eloli.inkerbot.iirose.registry.IbGroupRegistrar
import com.eloli.inkerbot.iirose.util.message.IbTranslator
import javax.inject.Singleton

class IbGroup(
    val roomId:String,
    override val name: String,
    val storage:Storage
) : Group {
    override val identity: Identity = Identity.of(roomId)
    override val key: ResourceKey= KEY
    override fun sendMessage(message: MessageComponent) {
        InkerBot.eventManager.post(IbSendRoomMessage(IbTranslator.toIb(message),"ffffff"))
    }

    override fun toString(): String {
        return "IbGroup(roomId='$roomId', name='$name', storage=$storage, identity=$identity, key=$key)"
    }


    companion object {
        val KEY=ResourceKey.of("iirose","room")
        fun factory():Factory{
            return InkerBot.injector.getInstance(Factory::class.java)
        }
        fun of(roomId: String):Group{
            return factory().of(roomId)
        }

        fun current(): Group {
            return of(InkerBot.injector.getInstance(IbConfig::class.java).room)
        }
    }

    @Singleton
    class Factory {
        private val registrar = Registries.group.get(KEY) as IbGroupRegistrar
        fun of(roomId: String):Group{
            return registrar.set(roomId)
        }
    }

    class Storage {

    }
}