package com.eloli.inkerbot.core.model

import com.eloli.inkerbot.api.model.ConsoleSender
import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.util.Identity
import com.eloli.inkerbot.api.util.ResourceKey
import javax.inject.Singleton

@Singleton
class InkConsoleSender : ConsoleSender {
  override fun sendMessage(message: MessageComponent) {
    println(message)
  }

  override val identity: Identity = Identity.of("inkerbot-console")
  override val name: String = "console"
  override val key: ResourceKey = ResourceKey.inkerbot("console")
}