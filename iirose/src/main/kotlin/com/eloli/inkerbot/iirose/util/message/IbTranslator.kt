package com.eloli.inkerbot.iirose.util.message

import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.model.message.MuiltComponent
import com.eloli.inkerbot.api.model.message.PlainTextComponent

object IbTranslator {
    fun toIb(message: MessageComponent): String {
        return when (message) {
            is MuiltComponent -> {
                val result = StringBuilder()
                for (sub in message.subs) {
                    result.append(toIb(sub))
                }
                result.toString()
            }
            is PlainTextComponent -> {
                message.context
            }
            else -> {
                ""
            }
        }
    }
}