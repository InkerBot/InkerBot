package bot.inker.core

import bot.inker.api.InkerBot
import bot.inker.api.event.EventManager
import bot.inker.api.plugin.PluginManager
import bot.inker.core.setting.InkSetting
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InkFrame : bot.inker.api.Frame {
  override val logger: Logger
    get() = LoggerFactory.getLogger("inkerbot")
  override val classLoader: ClassLoader
    get() = bot.inker.api.InkerBot::class.java.classLoader

  @Inject
  override lateinit var self: bot.inker.core.InkerBotPluginContainer
  override val storagePath: Path = File("./storage").toPath()
  override val configPath: Path = File("./config").toPath()

  @Inject
  private lateinit var setting: InkSetting

  @Inject
  private lateinit var pluginManager: PluginManager

  @Inject
  private lateinit var eventManager: EventManager

  @Inject
  private lateinit var serviceManager: bot.inker.core.InkServiceManager

  fun init() {
    if (setting.banner) {
      bot.inker.core.BannerPrinter.print(System.out)
    }

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
}