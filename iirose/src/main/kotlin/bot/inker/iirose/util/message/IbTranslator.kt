package bot.inker.iirose.util.message

import bot.inker.api.model.message.MessageComponent
import bot.inker.api.model.message.MuiltComponent
import bot.inker.api.model.message.PlainTextComponent

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