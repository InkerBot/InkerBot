package bot.inker.iirose.model

import bot.inker.api.model.Member
import bot.inker.api.model.message.MessageComponent
import bot.inker.api.registry.Registries
import bot.inker.api.registry.UpdatableRegistrar
import bot.inker.api.util.Identity
import bot.inker.api.util.ResourceKey
import bot.inker.iirose.event.IbSendPrivateMessage
import bot.inker.iirose.util.message.IbTranslator
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

class IbMember(
  val record: Record
) : Member {
  val userId: String get() = record.userId
  override val identity: Identity = Identity.of(record.uuid)
  override val key: ResourceKey = KEY
  override val name: String get() = record.name

  override fun sendMessage(message: MessageComponent) {
    bot.inker.api.InkerBot.eventManager.post(IbSendPrivateMessage(userId, IbTranslator.toIb(message), "ffffff"))
  }

  companion object {
    val KEY = ResourceKey.of("iirose", "member")
    val registrar: UpdatableRegistrar<Member, IbMember, Record>
      get() {
        return Registries.member.get(KEY) as UpdatableRegistrar<Member, IbMember, Record>
      }

    fun of(userId: String): Optional<Member> {
      return registrar.get(Identity.of(KEY.toString()+userId))
    }

    fun update(userId: String, command: (Record) -> Unit): IbMember {
      val identity = Identity.of(KEY.toString()+userId)
      return registrar.update(identity) {
        it.uuid = identity.uuid
        it.userId = userId
        command(it)
      }
    }
  }


  @Entity(name = "ib_member_record")
  class Record : Cloneable {
    @Id
    @Column
    lateinit var uuid: UUID

    @Column
    lateinit var userId: String

    @Column
    lateinit var name: String

    @Column
    var avatar: String? = null

    @Column
    var userTag: String? = null

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (javaClass != other?.javaClass) return false

      other as Record

      if (uuid != other.uuid) return false
      if (userId != other.userId) return false
      if (name != other.name) return false
      if (avatar != other.avatar) return false
      if (userTag != other.userTag) return false

      return true
    }

    override fun hashCode(): Int {
      var result = uuid.hashCode()
      result = 31 * result + userId.hashCode()
      result = 31 * result + name.hashCode()
      result = 31 * result + (avatar?.hashCode() ?: 0)
      result = 31 * result + (userTag?.hashCode() ?: 0)
      return result
    }


  }
}