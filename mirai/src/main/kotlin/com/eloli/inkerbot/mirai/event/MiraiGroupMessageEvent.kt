package bot.inker.mirai.event

import bot.inker.api.event.EventContext
import bot.inker.api.event.message.MessageEvent
import bot.inker.api.model.message.MessageComponent
import bot.inker.mirai.model.MiraiGroupReceiver
import bot.inker.mirai.model.MiraiGroupSender
import bot.inker.mirai.util.MiraiMessageUtil
import net.mamoe.mirai.event.events.GroupMessageEvent

class MiraiGroupMessageEvent(
  handle: GroupMessageEvent
) : MessageEvent {
  override val sender: Sender = MiraiGroupSender(handle.sender)
  override val reply: Receiver = MiraiGroupReceiver(handle.group)
  override val message: MessageComponent = MiraiMessageUtil.toInk(handle.message)
  override val context: EventContext = EventContext.empty()
}