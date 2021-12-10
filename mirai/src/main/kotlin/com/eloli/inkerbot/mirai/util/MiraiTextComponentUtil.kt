package bot.inker.mirai.util

import bot.inker.api.model.message.PlainTextComponent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.toPlainText

object MiraiTextComponentUtil {
  fun toMirai(component: PlainTextComponent): Message {
    return component.context.toPlainText()
  }
}