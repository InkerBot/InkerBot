package bot.inker.core.service

import bot.inker.api.InkerBot
import bot.inker.api.command.permission
import bot.inker.api.event.AutoComponent
import bot.inker.api.event.EventHandler
import bot.inker.api.event.Order
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.event.message.MessageEvent
import bot.inker.api.model.Member
import bot.inker.api.model.message.PlainTextComponent
import bot.inker.api.service.CommandService
import bot.inker.api.service.HelpCommand
import bot.inker.core.InkConsoleStream
import bot.inker.core.InkFrame
import bot.inker.core.command.ThreadCommandDispatcher
import bot.inker.core.event.InkCommandExecuteEvent
import bot.inker.core.event.InkConsoleMessageEvent
import bot.inker.core.event.InkPerformEvent
import bot.inker.core.event.lifestyle.InkLifecycleEvent
import bot.inker.core.util.post
import com.eloli.inkcmd.Command
import com.eloli.inkcmd.builder.ArgumentBuilder
import com.eloli.inkcmd.context.CommandContext
import com.eloli.inkcmd.exceptions.CommandSyntaxException
import com.eloli.inkcmd.terminal.LoliTerminal
import com.eloli.inkcmd.terminal.ProcessResult
import com.eloli.inkcmd.terminal.buffer.TextStream
import com.eloli.inkcmd.tree.OptionalNode
import com.eloli.inkcmd.values.BoolValueType
import com.eloli.inkcmd.values.IntegerValueType
import org.jline.utils.AttributedStringBuilder
import org.jline.utils.AttributedStyle
import java.util.stream.Collectors
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.ceil

@Singleton
@AutoComponent
class InkCommandService : CommandService {
  @Inject
  private lateinit var inkConsoleStream: InkConsoleStream

  @Inject
  override lateinit var dispatcher: ThreadCommandDispatcher

  @Inject
  lateinit var frame: InkFrame

  lateinit var terminal: LoliTerminal<MessageEvent>
  lateinit var terminalThread: Thread

  fun init() {
    dispatcher.root.helpHandler = InkerBot(HelpCommand::class)
    terminal = LoliTerminal(
      dispatcher,
      { ProcessResult(it, InkConsoleMessageEvent(it)) },
      AttributedStringBuilder()
        .style(AttributedStyle().foreground(AttributedStyle.YELLOW))
        .append("> ")
        .toAnsi(),
      System.`in`,
      inkConsoleStream.proxiedStdout,
      true
    )
    System.setOut(inkConsoleStream.logout)
    System.setErr(inkConsoleStream.logerr)
    inkConsoleStream.output = TextStream {
      terminal.stdout.print(it)
    }
  }

  fun loop() {
    terminal.joinLoop()
  }

  override fun execute(source: MessageEvent, line: String) {
    try {
      val event = InkCommandExecuteEvent(
        source,
        dispatcher.parse(line, source)
      ).post()
      if(!event.cancelled){
        dispatcher.execute(event.parseResults)
      }
    } catch (e: CommandSyntaxException) {
      source.sendMessage(
        PlainTextComponent.of(
          e.message!!
        )
      )
    }
  }

  override fun execute(sender: Member, line: String) {
    execute(InkPerformEvent(sender,line),line)
  }

  @EventHandler(order = Order.PRE)
  fun pre(event: InkLifecycleEvent.Initialization) {
    init()
    event.registerCommand = {
      dispatcher.register(it)
    }
  }
  @EventHandler(order = Order.PRE)
  fun pre(event: InkLifecycleEvent.PostInitialization) {
    terminalThread = Thread(
      {
        InkerBot(InkCommandService::class).loop()
        frame.close()
      },
      "terminal"
    )
    terminalThread.start()
  }

  @EventHandler(order = Order.POST)
  fun onMessage(event: MessageEvent) {
    if (event.message.toString().startsWith('/')) {
      execute(event,event.message.toString().substring(1))
    }
  }

  fun getHelp(ctx: CommandContext<MessageEvent>) {
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
    ctx.source.sendMessage(PlainTextComponent.of(builder.toString()))
  }

  override fun <T : ArgumentBuilder<MessageEvent, T>> withSmartHelpOption(argument: ArgumentBuilder<MessageEvent, T>): T {
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

  override fun <T : ArgumentBuilder<MessageEvent, T>> withSmartHelpCommand(
    argument: ArgumentBuilder<MessageEvent, T>,
    command: Command<MessageEvent>
  ): T {
    argument.executes {
      if (it.getOption("help", Boolean::class.java)) {
        val builder = java.lang.StringBuilder()
        builder.appendLine("+++ Help text +++")
        builder.appendLine(it.nodes.last().node.describe)
        val smartUsage =
          InkerBot(CommandService::class).dispatcher.getSmartUsage(it.nodes.last().node, it.source)
        val prefix = it.nodes.stream()
          .map { it.node.usageText }
          .collect(Collectors.joining(" "))
        if (smartUsage.isNotEmpty()) {
          smartUsage.forEach { (k, v) ->
            builder.append(prefix).append(" ").append(v).append(" : ").appendLine(k.describe)
          }
        }
        for (node in it.nodes) {
          for (option in node.node.options.values) {
            builder.append("--").append(option.name).append(" <").append(option.type.toString()).append("> : ")
              .appendLine(option.describe)
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