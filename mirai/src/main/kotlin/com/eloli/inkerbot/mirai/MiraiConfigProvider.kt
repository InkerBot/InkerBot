package bot.inker.mirai

import bot.inker.api.config.ConfigService
import bot.inker.api.plugin.PluginContainer
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class MiraiConfigProvider @Inject constructor(
  pluginContainer: PluginContainer,
  configServiceFactory: ConfigService.Factory
) : Provider<MiraiConfig> {
  private val configService: ConfigService<MiraiConfig>
  private val setting: MiraiConfig

  override fun get(): MiraiConfig {
    return setting
  }

  init {
    configService = configServiceFactory.of(pluginContainer, "config", MiraiConfig::class.java)
    setting = configService.load()
  }
}