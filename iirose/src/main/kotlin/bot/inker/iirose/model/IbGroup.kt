package bot.inker.iirose.model

import bot.inker.api.InkerBot
import bot.inker.api.model.Group
import bot.inker.api.model.message.MessageComponent
import bot.inker.api.registry.Registries
import bot.inker.api.registry.UpdatableRegistrar
import bot.inker.api.util.Identity
import bot.inker.api.util.ResourceKey
import bot.inker.iirose.config.IbConfig
import bot.inker.iirose.event.IbSendRoomMessage
import bot.inker.iirose.util.message.IbTranslator
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

class IbGroup(
  val record: Record
) : Group {
  override val identity: Identity get() = Identity.of(record.uuid)
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
      return registrar.get(Identity.of(KEY.toString()+roomId))
    }

    fun update(roomId: String, command: (Record) -> Unit): Group {
      val identity = Identity.of(IbMember.KEY.toString()+roomId)
      return registrar.update(identity) {
        it.uuid = identity.uuid
        it.roomId = roomId
        command(it)
      }
    }

    fun current(): Group {
      return update(bot.inker.api.InkerBot.injector.getInstance(IbConfig::class.java).room) {
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
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (javaClass != other?.javaClass) return false

      other as Record

      if (uuid != other.uuid) return false
      if (roomId != other.roomId) return false
      if (name != other.name) return false

      return true
    }

    override fun hashCode(): Int {
      var result = uuid.hashCode()
      result = 31 * result + roomId.hashCode()
      result = 31 * result + name.hashCode()
      return result
    }


  }
}