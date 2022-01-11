package bot.inker.api

import bot.inker.api.plugin.PluginContainer
import bot.inker.api.service.CommandService
import bot.inker.api.service.SchedulerService
import bot.inker.api.tasker.Tasker
import org.slf4j.Logger
import java.nio.file.Path
import java.util.concurrent.Executor

interface Frame:Executor {
  val logger: Logger
  val classLoader: ClassLoader

  val self: PluginContainer

  val storagePath: Path
  val configPath: Path

  val asyncTasker: Tasker

  fun execute(action: () -> Unit)
  val commandService: CommandService
}