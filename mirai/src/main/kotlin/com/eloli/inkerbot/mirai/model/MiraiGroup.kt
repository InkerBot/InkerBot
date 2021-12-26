package com.eloli.inkerbot.mirai.model

import bot.inker.api.InkerBot
import bot.inker.api.model.Group
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

class MiraiGroup(
  private val record:Record
):Group {
  override fun sendMessage(message: MessageComponent) {
    runBlocking {
      InkerBot(MiraiCore::class).bot.getGroupOrFail(record.groupNumber.toLong()).sendMessage(
        MiraiTranslator.toMirai(message)
      )
    }
  }

  override val identity: Identity = Identity.of(record.uuid)
  override val key: ResourceKey = KEY
  override val name: String = record.name

  companion object {
    val KEY = ResourceKey.of("mirai","group")
    val registrar: UpdatableRegistrar<Group, MiraiGroup, Record>
      get() {
        return Registries.group.get(KEY) as UpdatableRegistrar<Group, MiraiGroup, Record>
      }

    fun of(group: net.mamoe.mirai.contact.Group): Group {
      return update(group.id.toString()){
        it.name = group.name
      }
    }

    fun of(groupNumber: String): Optional<Group> {
      return registrar.get(Identity.of(KEY.toString()+groupNumber))
    }

    fun update(groupNumber: String, command: (Record) -> Unit): MiraiGroup {
      val identity = Identity.of(KEY.toString() + groupNumber)
      return registrar.update(identity) {
        it.uuid = identity.uuid
        it.groupNumber = groupNumber
        command(it)
      }
    }
  }

  @Entity(name = "mirai_group_record")
  class Record:Cloneable {
    @Id
    @Column
    lateinit var uuid: UUID

    @Column
    lateinit var name: String

    @Column
    lateinit var groupNumber: String
  }
}