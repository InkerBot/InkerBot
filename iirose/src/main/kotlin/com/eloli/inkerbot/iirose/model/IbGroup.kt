package com.eloli.inkerbot.iirose.model

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.model.Group
import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.registry.Registries
import com.eloli.inkerbot.api.registry.UpdatableRegistrar
import com.eloli.inkerbot.api.util.Identity
import com.eloli.inkerbot.api.util.ResourceKey
import com.eloli.inkerbot.iirose.config.IbConfig
import com.eloli.inkerbot.iirose.event.IbSendRoomMessage
import com.eloli.inkerbot.iirose.util.message.IbTranslator
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

class IbGroup(
  val record: Record
) : Group {
  override val identity: Identity get() = Identity.Companion.of(record.uuid)
  override val key: ResourceKey = KEY
  override val name: String get() = record.name

  override fun sendMessage(message: MessageComponent) {
    InkerBot.eventManager.post(IbSendRoomMessage(IbTranslator.toIb(message), "ffffff"))
  }

  companion object {
    val KEY = ResourceKey.of("iirose", "room")
    val registrar: UpdatableRegistrar<Group, IbGroup, Record>
      get() {
        return Registries.group.get(KEY) as UpdatableRegistrar<Group, IbGroup, Record>
      }

    operator fun get(roomId: String): Optional<Group> {
      return registrar.get(Identity.of(roomId))
    }

    fun update(roomId: String, command: (Record) -> Unit): Group {
      return registrar.update(Identity.of(roomId)) {
        it.uuid = Identity.of(roomId).uuid
        it.roomId = roomId
        command(it)
      }
    }

    fun current(): Group {
      return update(InkerBot.injector.getInstance(IbConfig::class.java).room) {
        it.name = "current"
      }
    }
  }

  @Entity(name = "ib_group_record")
  class Record : Cloneable {
    @Id
    @Column
    lateinit var uuid: UUID
    @Column
    lateinit var roomId: String
    @Column
    lateinit var name: String
  }
}