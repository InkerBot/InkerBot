package com.eloli.inkerbot.core

import com.eloli.inkerbot.api.Frame
import com.eloli.inkerbot.api.config.ConfigService
import com.eloli.inkerbot.api.event.EventContextKey
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.plugin.*
import com.eloli.inkerbot.api.util.ResourceKey
import com.eloli.inkerbot.core.config.InkConfigService
import com.eloli.inkerbot.core.event.InkEventContextKey
import com.eloli.inkerbot.core.event.InkEventManager
import com.eloli.inkerbot.core.plugin.InkPluginDepend
import com.eloli.inkerbot.core.plugin.InkPluginManager
import com.eloli.inkerbot.core.plugin.InkPluginMeta
import com.eloli.inkerbot.core.plugin.InkPluginUrls
import com.eloli.inkerbot.core.setting.InkSetting
import com.eloli.inkerbot.core.setting.ImplSettingProvider
import com.eloli.inkerbot.core.util.InkResourceKey
import com.google.inject.Binder

class InkerBotModule : JvmPlugin {
    override fun configure(binder: Binder) {
        binder.bind(EventContextKey.Builder::class.java).to(InkEventContextKey.Builder::class.java)
        binder.bind(EventManager::class.java).to(InkEventManager::class.java)

        binder.bind(PluginDepend.Builder::class.java).to(InkPluginDepend.Builder::class.java)
        binder.bind(PluginManager::class.java).to(InkPluginManager::class.java)
        binder.bind(PluginMeta.Builder::class.java).to(InkPluginMeta.Builder::class.java)
        binder.bind(PluginUrls.Builder::class.java).to(InkPluginUrls.Builder::class.java)

        binder.bind(ResourceKey.Factory::class.java).to(InkResourceKey.Factory::class.java)

        binder.bind(ConfigService.Factory::class.java).to(InkConfigService.Factory::class.java)
        binder.bind(InkSetting::class.java).toProvider(ImplSettingProvider::class.java)

        binder.bind(Frame::class.java).to(InkFrame::class.java)
    }
}