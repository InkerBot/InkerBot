package com.eloli.inkerbot.iirose.event

import com.eloli.inkerbot.api.event.EventContext
import com.eloli.inkerbot.api.event.EventHandler
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.event.message.PrivateMessageEvent
import com.eloli.inkerbot.api.model.Member
import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.model.message.PlainTextComponent
import com.eloli.inkerbot.iirose.config.IbConfig
import com.eloli.inkerbot.iirose.model.IbMember
import javax.inject.Inject
import javax.inject.Singleton

class IbPrivateMessageEvent(
  message: String,
  val color: String,
  val id: String,
  userId: String,
  userName: String,
  avatar: String,
  val time: Long,
) : PrivateMessageEvent {
  override val context: EventContext = EventContext.empty()
  override val sender: Member = IbMember.update(userId) {
    it.name = userName
    it.avatar = avatar
  }
  override val message: MessageComponent = PlainTextComponent.of(message)


  override fun sendMessage(message: MessageComponent) {
    sender.sendMessage(message)
  }

  override fun toString(): String {
    return "IbPrivateMessageEvent(color='$color', id='$id', time=$time, sender=$sender, message=$message)"
  }

  // "1637396522>5e5f9bd4b3e62>InkerBot>http://r.iirose.com/i/21/9/17/0/1438-GH.jpg>imink>847fc1>>847fc1>1>>226225061655
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
        && event.split[0].startsWith("\"")
        && event.split[0].substring(1).toLong() > startAt
        && event.split[2] != config.username
      ) {
        eventManager.post(
          IbPrivateMessageEvent(
            event.split[4],
            event.split[5],
            event.split[10],
            event.split[1],
            event.split[2],
            event.split[3],
            event.split[0].substring(1).toLong()
          )
        )
      }
    }
  }
}