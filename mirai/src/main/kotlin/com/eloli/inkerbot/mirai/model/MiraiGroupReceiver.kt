package bot.inker.mirai.model

import bot.inker.api.model.message.MessageComponent
import bot.inker.api.util.Identity
import bot.inker.api.util.ResourceKey
import bot.inker.mirai.util.MiraiMessageUtil
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
