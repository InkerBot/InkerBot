package com.eloli.inkerbot.core.command

import com.eloli.inkcmd.CommandDispatcher
import com.eloli.inkcmd.builder.LiteralArgumentBuilder
import com.eloli.inkcmd.builder.ValuedArgumentBuilder
import com.eloli.inkcmd.context.CommandContext
import com.eloli.inkcmd.exceptions.CommandSyntaxException
import com.eloli.inkcmd.terminal.LoliTerminal
import com.eloli.inkcmd.terminal.ProcessResult
import com.eloli.inkcmd.values.IntegerValueType
import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.event.EventHandler
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.event.Order
import com.eloli.inkerbot.api.event.lifestyle.LifecycleEvent
import com.eloli.inkerbot.api.event.message.MessageEvent
import com.eloli.inkerbot.api.model.message.PlainTextComponent
import com.eloli.inkerbot.api.service.CommandService
import com.eloli.inkerbot.core.event.InkConsoleMessageEvent
import com.eloli.inkerbot.core.event.lifestyle.InkLifecycleEvent
import org.jline.utils.AttributedStringBuilder
import org.jline.utils.AttributedStyle
import javax.inject.Singleton
import kotlin.math.ceil

@Singleton
class InkCommandService : CommandService {
  override lateinit var dispatcher: CommandDispatcher<MessageEvent>
  lateinit var terminal: LoliTerminal<MessageEvent>

  fun init() {
    dispatcher = CommandDispatcher<MessageEvent>()
    terminal = LoliTerminal(
      dispatcher,
      { ProcessResult(it, InkConsoleMessageEvent(it)) },
      AttributedStringBuilder()
        .style(AttributedStyle().foreground(AttributedStyle.YELLOW))
        .append("> ")
        .toAnsi(),
      System.`in`,
      System.out,
      true
    )
  }

  fun loop() {
    terminal.joinLoop();
  }

  override fun execute(line: String, source: MessageEvent) {
    try {
      dispatcher.execute(line, source)
    } catch (e: CommandSyntaxException) {
      source.sendMessage(
        PlainTextComponent.of(
          e.message!!
        )
      )
    }
  }

  @EventHandler
  fun onInit(event: LifecycleEvent.Enable) {
    InkerBot(EventManager::class).post(
      InkLifecycleEvent.RegisterCommand {
        dispatcher.register(it)
      }
    )
  }

  @EventHandler(order = Order.POST)
  fun onMessage(event: MessageEvent) {
    if (event.message.toString().startsWith('/')) {
      execute(event.message.toString().substring(1), event)
    }
  }

  @EventHandler()
  fun registerHelp(event: LifecycleEvent.RegisterCommand) {
    event.register(
      LiteralArgumentBuilder.literal<MessageEvent>("help")
        .setDescribe("Get list of commands.")
        .executes(this::getHelp)
        .then(
          ValuedArgumentBuilder
            .argument<MessageEvent, Int>("page", IntegerValueType.integer(1))
            .executes(this::getHelp)
            .setDescribe("The page of list")
        )
        .build()
    )
  }

  fun getHelp(ctx: CommandContext<MessageEvent>): Int {
    val helps = dispatcher.root.children.map { it.name + " : " + it.describe }
    val page = try {
      ctx.getArgument("page", Int::class.java)
    } catch (e: IllegalArgumentException) {
      1
    }
    val builder = StringBuilder()
    builder.appendLine("+++ Command list +++")
    builder.appendLine("Page ($page/${ceil(helps.size / 10.0).toInt()})")
    builder.appendLine()
    for (i in (page - 1) * 10 until Math.min(page * 10, helps.size)) {
      builder.appendLine(helps[i])
    }
    ctx.source.sendMessage(PlainTextComponent.of(builder.toString()));
    return 1
  }
}