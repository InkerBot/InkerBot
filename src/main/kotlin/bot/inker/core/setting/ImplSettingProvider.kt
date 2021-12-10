package bot.inker.core.setting

import bot.inker.api.config.ConfigService
import bot.inker.core.InkerBotPluginContainer
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ImplSettingProvider @Inject constructor(
  pluginContainer: InkerBotPluginContainer,
  configServiceFactory: ConfigService.Factory
) : Provider<InkSetting> {
  private val configService: ConfigService<InkSetting>
  private val setting: InkSetting
  override fun get(): InkSetting {
    return setting
  }

  init {
    configService = configServiceFactory.of(pluginContainer, "config", InkSetting::class.java)
    setting = configService.load()
  }
}