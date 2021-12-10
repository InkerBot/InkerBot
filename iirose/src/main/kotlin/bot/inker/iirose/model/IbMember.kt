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
  override val identity: Identity = Identity.of(userId)
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
      return registrar.get(Identity.Companion.of(userId))
    }

    fun update(userId: String, command: (Record) -> Unit): IbMember {
      return registrar.update(Identity.of(userId)) {
        it.uuid = Identity.of(userId).uuid
        it.userId = userId
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
  }
}