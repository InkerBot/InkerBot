package com.eloli.inkerbot.iirose.event

import com.eloli.inkerbot.api.event.EventContext
import com.eloli.inkerbot.api.event.EventHandler
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.event.message.GroupMessageEvent
import com.eloli.inkerbot.api.model.Group
import com.eloli.inkerbot.api.model.Member
import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.model.message.PlainTextComponent
import com.eloli.inkerbot.iirose.config.IbConfig
import com.eloli.inkerbot.iirose.model.IbGroup
import com.eloli.inkerbot.iirose.model.IbMember
import javax.inject.Inject
import javax.inject.Singleton

class IbGroupMessageEvent(
  message: String,
  val color: String,
  val id: String,
  val userId: String,
  val userName: String,
  val userTag: String,
  val avatar: String,
  val time: Long
) : GroupMessageEvent {
  override val context: EventContext = EventContext.empty()
  override val sender: Member = IbMember.update(userId) {
    it.name = userName
    it.avatar = avatar
    it.userTag = userTag
  }
  override val group: Group = IbGroup.current()

  override val message: MessageComponent = PlainTextComponent.of(message)
  override fun sendMessage(message: MessageComponent) {
    group.sendMessage(message)
  }

  override fun toString(): String {
    return "IbGroupMessageEvent(color='$color', id='$id', userId='$userId', userName='$userName', userTag='$userTag', avatar='$avatar', time=$time, sender=$sender, group=$group, message=$message)"
  }

  @Singleton
  class Resolver {
    private val startAt = System.currentTimeMillis() / 1000

    @Inject
    private lateinit var config: IbConfig

    @Inject
    private lateinit var eventManager: EventManager

    @EventHandler
    fun onMessage(event: IbRawMessageEvent) {
      if (event.split.size == 11
        && !event.split[0].startsWith("\"")
        && event.split[0].toLong() > startAt
        && event.split[2] != config.username
      ) {
        eventManager.post(
          IbGroupMessageEvent(
            event.split[3],
            event.split[4],
            event.split[10],
            event.split[8],
            event.split[2],
            event.split[9],
            event.split[1],
            event.split[0].toLong()
          )
        )
      }
    }
  }
}