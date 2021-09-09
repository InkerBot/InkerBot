package com.eloli.inkerbot.mirai

import com.eloli.inkerbot.api.config.ConfigService
import com.eloli.inkerbot.api.plugin.PluginContainer
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