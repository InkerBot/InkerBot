package bot.inker.mirai

import bot.inker.api.InkerBot
import bot.inker.mirai.handler.GroupMessageEventHandler
import net.mamoe.mirai.Bot

object MiraiHandler : Handler {
  override fun register(bot: Bot) {
    InkerBot.injector.getInstance(GroupMessageEventHandler::class.java).register(bot)
  }
}