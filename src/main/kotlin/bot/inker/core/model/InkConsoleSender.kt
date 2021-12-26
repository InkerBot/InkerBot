package bot.inker.core.model

import bot.inker.api.model.ConsoleSender
import bot.inker.api.model.message.MessageComponent
import bot.inker.api.service.CommandService
import bot.inker.api.util.Identity
import bot.inker.api.util.ResourceKey
import bot.inker.core.command.InkCommandService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InkConsoleSender : ConsoleSender {
  @Inject
  lateinit var commandService:InkCommandService
  override fun sendMessage(message: MessageComponent) {
    commandService.print(message.toString()+"\n")
  }

  override val identity: Identity = Identity.of(KEY.toString()+ NAME)
  override val name: String = NAME
  override val key: ResourceKey = KEY
  companion object {
    val KEY: ResourceKey = ResourceKey.inkerbot("console")
    val NAME: String = "inkerbot-console"
  }
}