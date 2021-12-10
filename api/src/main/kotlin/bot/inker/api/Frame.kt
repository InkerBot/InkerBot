package bot.inker.api

import bot.inker.api.plugin.PluginContainer
import org.slf4j.Logger
import java.nio.file.Path

interface Frame {
  val logger: Logger
  val classLoader: ClassLoader

  val self: PluginContainer

  val storagePath: Path
  val configPath: Path
}