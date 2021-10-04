package com.eloli.inkerbot.mirai.event

import com.eloli.inkerbot.api.event.EventContext
import com.eloli.inkerbot.api.event.message.MessageEvent
import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.mirai.model.MiraiGroupReceiver
import com.eloli.inkerbot.mirai.model.MiraiGroupSender
import com.eloli.inkerbot.mirai.util.MiraiMessageUtil
import net.mamoe.mirai.event.events.GroupMessageEvent

class MiraiGroupMessageEvent(
    handle: GroupMessageEvent
) : MessageEvent {
    override val sender: Sender = MiraiGroupSender(handle.sender)
    override val reply: Receiver = MiraiGroupReceiver(handle.group)
    override val message: MessageComponent = MiraiMessageUtil.toInk(handle.message)
    override val context: EventContext = EventContext.empty()
}