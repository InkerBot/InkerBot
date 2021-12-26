package com.eloli.inkerbot.mirai.model

import bot.inker.api.InkerBot
import bot.inker.api.model.Member
import bot.inker.api.model.message.MessageComponent
import bot.inker.api.registry.Registries
import bot.inker.api.registry.UpdatableRegistrar
import bot.inker.api.util.Identity
import bot.inker.api.util.ResourceKey
import com.eloli.inkerbot.mirai.MiraiCore
import com.eloli.inkerbot.mirai.util.message.MiraiTranslator
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.User
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

class MiraiMember(
  private val record:Record
):Member {
  override fun sendMessage(message: MessageComponent) {
    runBlocking {
      var user:User? = InkerBot(MiraiCore::class).bot.getFriend(record.qqNumber.toLong())
      if(user == null){
        user = InkerBot(MiraiCore::class).bot.getStrangerOrFail(record.qqNumber.toLong())
      }
      user.sendMessage(
        MiraiTranslator.toMirai(message)
      )
    }
  }

  override val identity: Identity = Identity.of(record.uuid)
  override val key: ResourceKey = KEY
  override val name: String = record.name
  val qqNumber: String = record.qqNumber

  companion object {
    val KEY = ResourceKey.of("mirai","member")
    val registrar: UpdatableRegistrar<Member, MiraiMember, Record>
      get() {
        return Registries.member.get(KEY) as UpdatableRegistrar<Member, MiraiMember, Record>
      }

    fun of(user: User): Member {
      return update(user.id.toString()){
        it.name = user.nick
      }
    }

    fun of(userId: String): Optional<Member> {
      return registrar.get(Identity.of(KEY.toString()+userId))
    }

    fun update(qqNumber: String, command: (Record) -> Unit): MiraiMember {
      val identity = Identity.of(KEY.toString() + qqNumber)
      return registrar.update(identity) {
        it.uuid = identity.uuid
        it.qqNumber = qqNumber
        command(it)
      }
    }
  }

  @Entity(name = "mirai_member_record")
  class Record:Cloneable {
    @Id
    @Column
    lateinit var uuid: UUID

    @Column
    lateinit var name: String

    @Column
    lateinit var qqNumber: String
  }
}