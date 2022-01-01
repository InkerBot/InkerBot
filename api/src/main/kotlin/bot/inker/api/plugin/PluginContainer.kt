package bot.inker.api.plugin

import org.slf4j.Logger
import java.net.URL
import java.nio.file.Path

interface PluginContainer {
  val name: String
  val meta: PluginMeta
  val loader: ClassLoader
  val logger: Logger
  val dataPath: Path
  val configPath: Path
  val enabled: Boolean

  fun addDepend(depend: PluginContainer)
  fun addDepend(classURL: URL)

  @Throws(Exception::class)
  fun load()

  @Throws(Exception::class)
  fun enable()

  @Throws(Exception::class)
  fun disable()
}