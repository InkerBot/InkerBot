package com.eloli.inkerbot.core.command

import com.eloli.inkcmd.Command
import com.eloli.inkcmd.CommandDispatcher
import com.eloli.inkcmd.builder.ArgumentBuilder
import com.eloli.inkcmd.builder.LiteralArgumentBuilder
import com.eloli.inkcmd.builder.ValuedArgumentBuilder
import com.eloli.inkcmd.context.CommandContext
import com.eloli.inkcmd.exceptions.CommandSyntaxException
import com.eloli.inkcmd.terminal.LoliTerminal
import com.eloli.inkcmd.terminal.ProcessResult
import com.eloli.inkcmd.tree.OptionalNode
import com.eloli.inkcmd.values.BoolValueType
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
import java.util.stream.Collectors
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

  override fun <T:ArgumentBuilder<MessageEvent,T>> withSmartHelpOption(argument:ArgumentBuilder<MessageEvent,T>):T{
    argument.withOption(
      OptionalNode.builder<MessageEvent>()
        .name("help")
        .type(BoolValueType.bool())
        .defaultValue(false)
        .defineValue(true)
        .describe("Show help for this command.")
        .build()
    )
    return argument as T
  }

  override fun <T:ArgumentBuilder<MessageEvent,T>> withSmartHelpCommand(argument:ArgumentBuilder<MessageEvent,T>, command: Command<MessageEvent>):T{
    argument.executes {
      if(it.getOption("help",Boolean::class.java)){
        val builder = java.lang.StringBuilder()
        builder.appendLine("+++ Help text +++")
        builder.appendLine(it.nodes.last().node.describe)
        val smartUsage =  InkerBot(CommandService::class).dispatcher.getSmartUsage(it.nodes.last().node,it.source)
        val prefix = it.nodes.stream()
          .map { it.node.usageText }
          .collect(Collectors.joining(" "))
        if(smartUsage.isNotEmpty()){
          smartUsage.forEach { (k, v) ->
            builder.append(prefix).append(" ").append(v).append(" : ").appendLine(k.describe)
          }
        }
        for(node in it.nodes){
          for (option in node.node.options.values) {
            builder.append("--").append(option.name).append(" <").append(option.type.toString()).append("> : ").appendLine(option.describe)
          }
        }

        it.source.sendMessage(PlainTextComponent.of(builder.toString()))
        return@executes 1
      }
      command.run(it)
    }
    return argument as T
  }
}