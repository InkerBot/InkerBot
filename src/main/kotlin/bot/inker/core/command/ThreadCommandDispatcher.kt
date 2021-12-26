package bot.inker.core.command

import bot.inker.api.InkerBot
import bot.inker.api.event.message.MessageEvent
import bot.inker.core.InkFrame
import com.eloli.inkcmd.CommandDispatcher
import com.eloli.inkcmd.ParseResults

class ThreadCommandDispatcher : CommandDispatcher<MessageEvent>() {
  override fun execute(parse: ParseResults<MessageEvent>): Int {
    InkerBot(InkFrame::class).execute{
      super.execute(parse)
    }
    return 114514 // No! DONT ASK WHY 114514, I DONT KNOW
  }
}