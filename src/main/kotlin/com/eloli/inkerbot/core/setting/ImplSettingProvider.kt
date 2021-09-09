package com.eloli.inkerbot.core.setting

import com.eloli.inkerbot.api.config.ConfigService
import com.eloli.inkerbot.core.InkerBotPluginContainer
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