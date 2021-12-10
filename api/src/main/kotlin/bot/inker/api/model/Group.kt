package bot.inker.api.model

import bot.inker.api.model.message.MessageComponent
import bot.inker.api.util.Identifiable
import bot.inker.api.util.Named

interface Group : Identifiable, Named {
  fun sendMessage(message: MessageComponent)
}