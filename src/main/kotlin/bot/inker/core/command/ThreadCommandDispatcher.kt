package bot.inker.core.command

import bot.inker.api.Frame
import bot.inker.api.InkerBot
import bot.inker.api.event.EventManager
import bot.inker.api.event.message.ConsoleMessageEvent
import bot.inker.api.event.message.MessageEvent
import bot.inker.api.model.ConsoleSender
import bot.inker.api.model.message.PlainTextComponent
import bot.inker.api.service.CommandService
import bot.inker.core.InkFrame
import bot.inker.core.event.InkCommandExecuteEvent
import bot.inker.core.event.InkConsoleMessageEvent
import bot.inker.core.service.InkCommandService
import bot.inker.core.util.post
import com.eloli.inkcmd.CommandDispatcher
import com.eloli.inkcmd.ParseResults
import com.eloli.inkcmd.exceptions.CommandSyntaxException
import java.lang.Exception
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThreadCommandDispatcher : CommandDispatcher<MessageEvent>() {
  @Inject
  lateinit var frame: InkFrame
  @Inject
  lateinit var consoleSender: ConsoleSender

  override fun execute(input: String, source: MessageEvent): Int {
    if(source is ConsoleMessageEvent && Thread.currentThread() == InkerBot(InkCommandService::class).terminalThread){
      val later = CompletableFuture.supplyAsync({execute(input, source)},frame)
      try {
        try {
          return later.get()
        }catch (e:ExecutionException){
          throw e.cause!!
        }
      }catch (e:CommandSyntaxException){
        consoleSender.sendMessage(PlainTextComponent.of(e.message!!))
        return 1
      }
    }else{
      return super.execute(input, source)
    }
  }

  override fun execute(parse: ParseResults<MessageEvent>): Int {
    if(Thread.currentThread() == frame.mainThread) {
      return super.execute(parse)
    }else{
      val later = CompletableFuture.supplyAsync({execute(parse)},frame)
      try {
        return later.get()
      }catch (e:ExecutionException){
        throw e.cause!!
      }
    }
  }
}