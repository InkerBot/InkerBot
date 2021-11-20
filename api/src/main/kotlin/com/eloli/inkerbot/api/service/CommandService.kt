package com.eloli.inkerbot.api.service

import com.eloli.inkerbot.api.event.message.MessageEvent
import com.mojang.brigadier.CommandDispatcher

interface CommandService {
    val dispatcher: CommandDispatcher<MessageEvent>
}