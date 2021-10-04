package com.eloli.inkerbot.mirai.util

import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.model.message.MuiltComponent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain

object MiraiMuiltComponentUtil {
    fun toMirai(component: MuiltComponent): Message {
        var result: Message? = null
        for (sub in component.subs) {
            if (result == null) {
                result = MiraiMessageUtil.toMirai(sub)
            } else {
                result += MiraiMessageUtil.toMirai(sub)
            }
        }
        return result!!;
    }

    fun toInk(message: MessageChain): MessageComponent {
        val builder = MuiltComponent.builder()
        for (item in message) {
            builder.add(MiraiMessageUtil.toInk(item))
        }
        return builder.build()
    }
}