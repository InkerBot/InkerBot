package com.eloli.inkerbot.iirose.model

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.model.Group
import com.eloli.inkerbot.api.model.Member
import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.registry.Registries
import com.eloli.inkerbot.api.registry.UpdatableRegistrar
import com.eloli.inkerbot.api.util.Identity
import com.eloli.inkerbot.api.util.ResourceKey
import com.eloli.inkerbot.iirose.event.IbSendPrivateMessage
import com.eloli.inkerbot.iirose.util.message.IbTranslator
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

class IbMember(
    val record: Record
) :Member {
    val userId:String get() = record.userId
    override val identity: Identity = Identity.of(userId)
    override val key: ResourceKey= KEY
    override val name: String get() = record.name

    override fun sendMessage(message: MessageComponent) {
        InkerBot.eventManager.post(IbSendPrivateMessage(userId, IbTranslator.toIb(message),"ffffff"))
    }

    companion object{
        val KEY = ResourceKey.of("iirose","member")
        val registrar: UpdatableRegistrar<Member, IbMember, Record> get() {
            return Registries.member.get(KEY) as UpdatableRegistrar<Member, IbMember, Record>
        }
        fun of(userId:String):Optional<Member>{
            return registrar.get(Identity.Companion.of(userId))
        }
        fun update(userId: String, command: (Record)->Unit):IbMember {
            return registrar.update(Identity.of(userId)){
                it.uuid = Identity.of(userId).uuid
                it.userId = userId
            }
        }
    }


    @Entity(name = "ib_member_record")
    class Record{
        @Id
        @Column
        lateinit var uuid: UUID
        @Column
        lateinit var userId:String
        @Column
        lateinit var name:String
        @Column
        var avatar:String? = null
        @Column
        var userTag:String? = null
    }
}