package bot.inker.api

import bot.inker.api.plugin.PluginContainer
import bot.inker.api.service.CommandService
import org.slf4j.Logger
import java.nio.file.Path
import javax.inject.Inject

interface Frame {
  val logger: Logger
  val classLoader: ClassLoader

  val self: PluginContainer

  val storagePath: Path
  val configPath: Path

  fun execute(action: () -> Unit)
  val commandService: CommandService
}