package com.eloli.inkerbot.mirai

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.mirai.handler.GroupMessageEventHandler
import net.mamoe.mirai.Bot

object MiraiHandler : Handler {
    override fun register(bot: Bot) {
        InkerBot.injector.getInstance(GroupMessageEventHandler::class.java).register(bot)
    }
}