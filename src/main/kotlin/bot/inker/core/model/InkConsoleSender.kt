package bot.inker.core.model

import bot.inker.api.ConsoleStream
import bot.inker.api.model.ConsoleSender
import bot.inker.api.model.message.MessageComponent
import bot.inker.api.util.Identity
import bot.inker.api.util.ResourceKey
import bot.inker.core.InkConsoleStream
import bot.inker.core.service.InkCommandService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InkConsoleSender : ConsoleSender {
  @Inject
  lateinit var printStream: ConsoleStream

  override fun sendMessage(message: MessageComponent) {
    printStream.console.println(message.toString())
  }

  override val identity: Identity = Identity.of(KEY.toString()+ NAME)
  override val name: String = NAME
  override val key: ResourceKey = KEY
  companion object {
    val KEY: ResourceKey = ResourceKey.inkerbot("console")
    val NAME: String = "inkerbot-console"
  }
}