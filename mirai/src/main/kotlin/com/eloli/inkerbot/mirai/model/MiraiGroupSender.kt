package bot.inker.mirai.model

import bot.inker.api.model.message.MessageComponent
import bot.inker.api.util.Identity
import bot.inker.api.util.ResourceKey
import bot.inker.mirai.util.MiraiMessageUtil
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.nameCardOrNick

class MiraiGroupSender(private val sender: Member) : Sender {
  override val name: String = sender.nameCardOrNick
  override fun sendMessage(message: MessageComponent) {
    runBlocking {
      sender.sendMessage(MiraiMessageUtil.toMirai(message))
    }
  }

  override val identity: Identity = Identity.of(sender.id.toString())
  override val key: ResourceKey = ResourceKey.of("mirai", "member")
}