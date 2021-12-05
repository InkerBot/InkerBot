package com.eloli.inkerbot.mirai.util

import com.eloli.inkerbot.api.model.message.PlainTextComponent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.toPlainText

object MiraiTextComponentUtil {
  fun toMirai(component: PlainTextComponent): Message {
    return component.context.toPlainText()
  }
}