package bot.inker.core.model

import bot.inker.api.model.ConsoleSender
import bot.inker.api.model.message.MessageComponent
import bot.inker.api.util.Identity
import bot.inker.api.util.ResourceKey
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