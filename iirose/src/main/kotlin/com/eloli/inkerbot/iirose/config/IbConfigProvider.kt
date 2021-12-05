package com.eloli.inkerbot.iirose.config

import com.eloli.inkerbot.api.config.ConfigService
import com.eloli.inkerbot.api.plugin.PluginContainer
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class IbConfigProvider @Inject constructor(
  pluginContainer: PluginContainer,
  configServiceFactory: ConfigService.Factory
) : Provider<IbConfig> {
  private val configService: ConfigService<IbConfig>
  private val setting: IbConfig

  override fun get(): IbConfig {
    return setting
  }

  fun save() {
    configService.save(setting)
  }

  init {
    configService = configServiceFactory.of(pluginContainer, "config", IbConfig::class.java)
    setting = configService.load()
  }
}
