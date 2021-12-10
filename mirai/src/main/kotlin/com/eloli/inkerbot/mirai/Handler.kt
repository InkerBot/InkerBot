package bot.inker.mirai

import net.mamoe.mirai.Bot

interface Handler {
  fun register(bot: Bot)
}