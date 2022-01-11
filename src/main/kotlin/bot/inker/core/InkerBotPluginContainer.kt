package bot.inker.core

import bot.inker.api.InkerBot
import bot.inker.api.plugin.PluginContainer
import bot.inker.api.plugin.PluginMeta
import bot.inker.core.util.ReadPluginJson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.InputStreamReader
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Path
import javax.inject.Singleton

@Singleton
class InkerBotPluginContainer : PluginContainer {
  override val name: String = "inkerbot"
  override val meta: PluginMeta = ReadPluginJson.read(
    InputStreamReader(
      InkerBot::class.java.classLoader.getResourceAsStream("META-INF/plugin.json")!!
    )
  )
  override val loader: ClassLoader = InkerBot::class.java.classLoader
  override val logger: Logger = LoggerFactory.getLogger("plugin@inkerbot")
  override val dataPath: Path = File("./storage/inkerbot").toPath()
  override val configPath: Path = File("./config/inkerbot").toPath()
  override val enabled: Boolean = true

  override fun addDepend(depend: PluginContainer) {
    //
  }

  override fun addDepend(classURL: URL) {
    //
  }

  override fun load() {
    //
  }

  override fun enable() {
    //
  }

  override fun disable() {
    //
  }


  override fun toString(): String {
    return "MemPlugin@inkerbot"
  }
}