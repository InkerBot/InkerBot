package bot.inker.mirai

import bot.inker.api.event.EventContext

class MiraiBoxEvent(val mirai: net.mamoe.mirai.event.Event) : bot.inker.api.event.Event {
  override val context: EventContext = EventContext.empty()
}