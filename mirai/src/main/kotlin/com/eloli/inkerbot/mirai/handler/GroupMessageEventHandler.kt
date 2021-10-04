package com.eloli.inkerbot.mirai.handler

import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.mirai.Handler
import com.eloli.inkerbot.mirai.event.MiraiGroupMessageEvent
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