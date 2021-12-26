package com.eloli.inkerbot.mirai.event

import bot.inker.api.event.*
import bot.inker.api.event.message.MessageEvent
import bot.inker.api.event.message.PrivateMessageEvent
import bot.inker.api.model.Member
import bot.inker.api.model.message.MessageComponent
import bot.inker.api.model.message.PlainTextComponent
import com.eloli.inkerbot.mirai.model.MiraiMember
import com.eloli.inkerbot.mirai.util.message.MiraiTranslator
import net.mamoe.mirai.contact.User
import javax.inject.Inject
import javax.inject.Singleton

class MiraiStrangeMessageEvent(
  user: User,
  override val message: MessageComponent
): PrivateMessageEvent,MiraiMessageEvent {
  override var cancelled: Boolean = false
  override val sender: Member = MiraiMember.of(user)
  override fun sendMessage(message: MessageComponent) {
    sender.sendMessage(message)
  }

  override fun toString(): String {
    return "MiraiStrangeMessageEvent(message=$message, sender=$sender)"
  }

  override val context: EventContext = EventContext.empty()


  @AutoComponent
  @Singleton
  class Resolver{
    @Inject
    private lateinit var eventManager: EventManager

    @EventHandler(order = Order.POST)
    fun onMiraiEvent(e:MiraiBoxEvent){
      if (e.event is net.mamoe.mirai.event.events.StrangerMessageEvent) {
        eventManager.post(MiraiStrangeMessageEvent(
          e.event.sender,
          MiraiTranslator.toInk(e.event.message)
        ))
      }
    }
  }
}