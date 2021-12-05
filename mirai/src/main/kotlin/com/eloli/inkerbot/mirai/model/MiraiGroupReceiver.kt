package com.eloli.inkerbot.mirai.model

import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.util.Identity
import com.eloli.inkerbot.api.util.ResourceKey
import com.eloli.inkerbot.mirai.util.MiraiMessageUtil
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Group

class MiraiGroupReceiver(val group: Group) : Receiver {
  override fun sendMessage(message: MessageComponent) {
    runBlocking {
      group.sendMessage(MiraiMessageUtil.toMirai(message))
    }
  }

  override val identity: Identity = Identity.of(group.id.toString())
  override val key: ResourceKey = ResourceKey.of("mirai", "group")
  override val name: String = group.name
}
