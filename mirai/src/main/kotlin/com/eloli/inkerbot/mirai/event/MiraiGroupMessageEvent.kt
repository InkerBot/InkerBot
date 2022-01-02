package com.eloli.inkerbot.mirai.event

import bot.inker.api.event.*
import bot.inker.api.event.message.GroupMessageEvent
import bot.inker.api.model.Group
import bot.inker.api.model.Member
import bot.inker.api.model.message.MessageComponent
import com.eloli.inkerbot.mirai.config.MiraiConfig
import com.eloli.inkerbot.mirai.model.MiraiGroup
import com.eloli.inkerbot.mirai.model.MiraiMember
import com.eloli.inkerbot.mirai.util.message.MiraiTranslator
import net.mamoe.mirai.contact.User
import javax.inject.Inject
import javax.inject.Singleton

class MiraiGroupMessageEvent(
  group: net.mamoe.mirai.contact.Group,
  user: User,
  override val message: MessageComponent
): GroupMessageEvent,MiraiMessageEvent {
  override var cancelled: Boolean = false
  override val sender: Member = MiraiMember.of(user)
  override val group: Group = MiraiGroup.of(group)

  override fun sendMessage(message: MessageComponent) {
    group.sendMessage(message)
  }

  override fun toString(): String {
    return "MiraiGroupMessageEvent(message=$message, sender=$sender, group=$group)"
  }

  override val context: EventContext = EventContext.empty()

  @AutoComponent
  @Singleton
  class Resolver{
    @Inject
    private lateinit var eventManager: EventManager
    @Inject
    private lateinit var config:MiraiConfig

    @EventHandler(order = Order.POST)
    fun onMiraiEvent(e:MiraiBoxEvent){
      if (e.event is net.mamoe.mirai.event.events.GroupMessageEvent) {
        if(config.disabledGroups.contains(e.event.group.id.toString())){
          return
        }
        eventManager.post(MiraiGroupMessageEvent(
          e.event.group,
          e.event.sender,
          MiraiTranslator.toInk(e.event.message)
        ))
      }
    }
  }
}