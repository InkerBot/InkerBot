package bot.inker.api.event.message

import bot.inker.api.model.Group

interface GroupMessageEvent : MessageEvent {
  val group: Group
}