package com.eloli.inkerbot.mirai.util

import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.model.message.MuiltComponent
import com.eloli.inkerbot.api.model.message.PlainTextComponent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.toPlainText

object MiraiMessageUtil {
  fun toMirai(component: MessageComponent): Message {
    if (component is MuiltComponent) {
      return MiraiMuiltComponentUtil.toMirai(component)
    } else if (component is PlainTextComponent) {
      return MiraiTextComponentUtil.toMirai(component)
    } else {
      return component::class.simpleName!!.toPlainText()
    }
  }

  fun toInk(message: Message): MessageComponent {
    if (message is MessageChain) {
      return MiraiMuiltComponentUtil.toInk(message)
    }
    return PlainTextComponent.of(message.contentToString())
  }
}