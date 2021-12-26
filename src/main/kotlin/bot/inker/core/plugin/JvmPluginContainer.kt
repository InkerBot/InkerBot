package bot.inker.core.plugin

import bot.inker.api.InkerBot
import bot.inker.api.plugin.JvmPlugin
import bot.inker.api.plugin.PluginContainer
import bot.inker.api.plugin.PluginDepend
import bot.inker.api.plugin.PluginMeta
import bot.inker.core.dependency.DependencyResolver
import bot.inker.core.util.ReadPluginJson
import bot.inker.core.util.StaticEntryUtil
import com.google.gson.Gson
import com.google.inject.Injector
import com.google.inject.Module
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.InvocationTargetException
import java.nio.file.Files
import java.nio.file.Path

class JvmPluginContainer(val jarFile: File) : PluginContainer {
  private val gson: Gson = Gson()
  lateinit var injector: Injector
  override lateinit var loader: JvmPluginClassloader
  override lateinit var meta: PluginMeta
  override lateinit var logger: Logger

  override val name: String
    get() = meta.name
  override val dataPath: Path
    get() {
      return Files.createDirectories(InkerBot.frame.storagePath.resolve(name))
    }
  override val configPath: Path
    get() {
      return Files.createDirectories(InkerBot.frame.configPath.resolve(name))
    }

  override var enabled: Boolean = false

  override fun addDepend(depend: PluginContainer) {
    require(depend is JvmPluginContainer) { "Only java plugin could depend on." }
    loader.addDepend(depend.loader)
  }

  override fun load() {
    this.loader = JvmPluginClassloader(jarFile.toURI().toURL(), InkerBot.frame.classLoader)
    loadMeta()
    val dependencyResolver = InkerBot.injector.getInstance(DependencyResolver::class.java)
    for (depend in meta.depends) {
      if (depend.type == PluginDepend.Type.LIBRARY) {
        this.loader.addURL(
          dependencyResolver.getDependencyFile(depend.name).toURI().toURL()
        )
      }
    }
  }

  private fun loadMeta() {
    val metaStream: InputStream = this.loader.getResourceAsStream("META-INF/plugin.json")
      ?: throw FileNotFoundException("No META-INF/plugin.json found in $jarFile")
    val metaReader = InputStreamReader(metaStream)
    meta = ReadPluginJson.read(metaReader)
    metaReader.close()
    metaStream.close()
  }

  override fun enable() {
    enabled = true
    logger = LoggerFactory.getLogger("plugin@$name")
    val mainClass = loader.loadClass(meta.main)
    val jvmPlugin: JvmPlugin = (mainClass.getConstructor().newInstance() as JvmPlugin)
    injector = InkerBot.injector
      .createChildInjector(Module { binder ->
        binder.bind(PluginContainer::class.java).toInstance(this)
        binder.bind(Logger::class.java).toInstance(logger)
        try {
          jvmPlugin.configure(binder)
        } catch (e: InstantiationException) {
          logger.warn("Failed to constructor {}'s Module class {}.", this.name, mainClass.name)
        } catch (e: IllegalAccessException) {
          logger.warn("Failed to constructor {}'s Module class {}.", this.name, mainClass.name)
        } catch (e: InvocationTargetException) {
          logger.warn("Failed to constructor {}'s Module class {}.", this.name, mainClass.name)
        } catch (e: NoSuchMethodException) {
          logger.warn("Failed to constructor {}'s Module class {}.", this.name, mainClass.name)
        }
      })
    StaticEntryUtil.applyInjector(loader, injector)
    injector.injectMembers(jvmPlugin)
    InkerBot.eventManager.scanListeners(
      this,
      loader,
      jarFile.toURI().toURL()
    )
  }

  override fun disable() {
    enabled = false
    InkerBot.eventManager.unregisterPluginListeners(this)
  }

  override fun toString(): String {
    return "JvmPlugin@$name"
  }
}