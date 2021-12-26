package bot.inker.iirose.util.message

import bot.inker.api.model.message.AtComponent
import bot.inker.api.model.message.MessageComponent
import bot.inker.api.model.message.MuiltComponent
import bot.inker.api.model.message.PlainTextComponent
import bot.inker.iirose.model.IbMember
import bot.inker.iirose.util.CodeUtil

object IbTranslator {
  fun toIb(message: MessageComponent): String {
    return toIb(message, StringBuilder()).toString()
  }
  fun toIb(message: MessageComponent,builder: StringBuilder): StringBuilder {
    when (message) {
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
      is AtComponent -> {
        val target = message.target
        if(target is IbMember){
          builder.append(" [*${target.name}*] ")
        }else{
          builder.append(" [inkat,\"${target.key}\",\"${target.identity}\"] ")
        }
      }
    }
    return builder
  }

  fun toInk(message: String): MessageComponent {
    return PlainTextComponent.of(message)
  }
}