package bot.inker.core

import bot.inker.api.Frame
import bot.inker.api.InkerBot
import bot.inker.api.event.AutoComponent
import bot.inker.api.event.EventHandler
import bot.inker.api.event.EventManager
import bot.inker.api.event.Order
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.event.message.MessageEvent
import bot.inker.api.model.Member
import bot.inker.api.plugin.PluginManager
import bot.inker.api.registry.Registrar
import bot.inker.api.service.CommandService
import bot.inker.api.service.DatabaseService
import bot.inker.core.event.lifestyle.InkLifecycleEvent
import bot.inker.core.registry.InkConsoleMemberRegistry
import bot.inker.core.service.H2DatabaseService
import bot.inker.core.setting.InkSetting
import bot.inker.core.util.post
import com.eloli.inkcmd.builder.LiteralArgumentBuilder
import com.google.inject.TypeLiteral
import com.google.inject.name.Names
import org.jline.reader.UserInterruptException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
@AutoComponent
class InkFrame : Frame {
  override val logger: Logger
    get() = LoggerFactory.getLogger("inkerbot")
  override val classLoader: ClassLoader
    get() = InkerBot::class.java.classLoader

  @Inject
  override lateinit var self: InkerBotPluginContainer
  override val storagePath: Path = File("./storage").toPath()
  override val configPath: Path = File("./config").toPath()

  @Inject
  override lateinit var commandService: CommandService

  lateinit var mainThread:Thread

  private var normalExit = false

  private val queue: BlockingQueue<Runnable> = LinkedBlockingDeque();
  override fun execute(action: () -> Unit) {
    queue.put(action)
  }

  override fun execute(command: Runnable) {
    queue.put(command)
  }

  fun start(){
    mainThread = Thread(
      {
        while (true){
          try {
            queue.take().run()
          }catch (e:InterruptedException){
            if(!normalExit){
              throw e
            }else{
              InkerBot(InkLifecycleEvent.ServerStopping::class).post()
              InkerBot(InkLifecycleEvent.ServerStopped::class).post()
              InkerBot(InkLifecycleEvent.BotStopping::class).post()
              InkerBot(InkLifecycleEvent.BotStopped::class).post()
            }
          }
        }
      },
      "InkerBot-Main"
    )
    mainThread.start()
    mainThread.join()
  }

  fun close(){
    normalExit = true
    mainThread.interrupt()
    System.exit(0)
  }

  init {
    execute {
      eventManager.scanListeners(
        self,
        self.loader,
        InkerBot::class.java.protectionDomain.codeSource.location,
        InkFrame::class.java.protectionDomain.codeSource.location
      )
      val classes:Array<KClass<*>> = arrayOf(
        InkLifecycleEvent.Construction::class,
        InkLifecycleEvent.PreInitialization::class,
        InkLifecycleEvent.Initialization::class,
        InkLifecycleEvent.PostInitialization::class,
        InkLifecycleEvent.LoadComplete::class,
        InkLifecycleEvent.ServerAboutToStart::class,
        InkLifecycleEvent.ServerStarting::class,
        InkLifecycleEvent.ServerStarted::class,
      )
      for (clazz in classes) {
        if ((InkerBot(clazz) as InkLifecycleEvent.BootEvent).post().cancelled){
          close()
          return@execute
        }
      }
    }
  }

  @Inject
  private lateinit var setting: InkSetting

  @Inject
  private lateinit var pluginManager: PluginManager

  @Inject
  private lateinit var eventManager: EventManager

  @Inject
  private lateinit var serviceManager: InkServiceManager

  @EventHandler(order = Order.PRE)
  fun loadAllPlugins(event: InkLifecycleEvent.Construction) {
    pluginManager.addPlugin(self)

    val pluginPath = File("./plugins").toPath()
    Files.createDirectories(pluginPath)
    Arrays.stream(
      Objects.requireNonNull<Array<File>>(pluginPath.toFile().listFiles())
    ).filter { file: File ->
      file.name.endsWith(".jar")
    }.forEach { file: File ->
      pluginManager.addPlugin(file)
    }

    pluginManager.load()
    pluginManager.enable()
  }

  @EventHandler
  fun onRegisterCommand(e: LifecycleEvent.RegisterCommand) {
    e.register(
      LiteralArgumentBuilder.literal<MessageEvent>("stop")
      .describe("Stop InkerBot")
      .executes { throw UserInterruptException("stop") }
      .build()
    )
  }

  @EventHandler
  fun onRegisterService(e: LifecycleEvent.RegisterService) {
    e.register {
      bind(DatabaseService::class.java).annotatedWith(Names.named("h2")).to(H2DatabaseService::class.java)
      bind(object : TypeLiteral<Registrar<Member>>() {}).annotatedWith(Names.named("inkerbot:console"))
        .to(InkConsoleMemberRegistry::class.java)
    }
  }
}