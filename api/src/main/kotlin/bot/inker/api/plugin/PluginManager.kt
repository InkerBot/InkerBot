package bot.inker.api.plugin

import java.io.File

interface PluginManager {
  val plugins: Collection<PluginContainer>
  fun addPlugin(pluginFile: File)
  fun addPlugin(plugin: PluginContainer)
  fun load()
  fun enable()
}