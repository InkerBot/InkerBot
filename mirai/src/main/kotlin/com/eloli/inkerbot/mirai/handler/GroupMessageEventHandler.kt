package bot.inker.mirai.handler

import bot.inker.api.event.EventManager
import bot.inker.mirai.Handler
import bot.inker.mirai.event.MiraiGroupMessageEvent
import com.google.inject.Inject
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.events.GroupMessageEvent

class GroupMessageEventHandler : Handler {
  @Inject
  private lateinit var eventManager: EventManager;
  override fun register(bot: Bot) {
    bot.eventChannel.subscribeAlways<GroupMessageEvent> {
      eventManager.post(MiraiGroupMessageEvent(this))
    }
  }
}