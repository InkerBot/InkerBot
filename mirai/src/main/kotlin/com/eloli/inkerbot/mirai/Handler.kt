package com.eloli.inkerbot.mirai

import net.mamoe.mirai.Bot

interface Handler {
    fun register(bot: Bot)
}