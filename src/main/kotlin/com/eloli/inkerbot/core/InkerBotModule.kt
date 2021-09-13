package com.eloli.inkerbot.core

import com.eloli.inkerbot.api.Frame
import com.eloli.inkerbot.api.ServiceManager
import com.eloli.inkerbot.api.config.ConfigService
import com.eloli.inkerbot.api.event.EventContextKey
import com.eloli.inkerbot.api.event.EventHandler
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.event.lifestyle.LifeStyleEvent
import com.eloli.inkerbot.api.model.message.AtComponent
import com.eloli.inkerbot.api.model.message.MuiltComponent
import com.eloli.inkerbot.api.model.message.PlainTextComponent
import com.eloli.inkerbot.api.plugin.*
import com.eloli.inkerbot.api.registry.Registrar
import com.eloli.inkerbot.api.registry.Registry
import com.eloli.inkerbot.api.service.DatabaseService
import com.eloli.inkerbot.api.util.Identity
import com.eloli.inkerbot.api.util.ResourceKey
import com.eloli.inkerbot.core.config.InkConfigService
import com.eloli.inkerbot.core.event.InkEventContextKey
import com.eloli.inkerbot.core.event.InkEventManager
import com.eloli.inkerbot.core.model.InkAt
import com.eloli.inkerbot.core.model.InkMuilt
import com.eloli.inkerbot.core.model.InkPlainText
import com.eloli.inkerbot.core.plugin.InkPluginDepend
import com.eloli.inkerbot.core.plugin.InkPluginManager
import com.eloli.inkerbot.core.plugin.InkPluginMeta
import com.eloli.inkerbot.core.plugin.InkPluginUrls
import com.eloli.inkerbot.core.registry.InkRegistry
import com.eloli.inkerbot.core.service.H2DatabaseService
import com.eloli.inkerbot.core.service.InkDatabaseService
import com.eloli.inkerbot.core.setting.InkSetting
import com.eloli.inkerbot.core.setting.ImplSettingProvider
import com.eloli.inkerbot.core.util.InkIdentity
import com.eloli.inkerbot.core.util.InkResourceKey
import com.google.inject.Binder
import com.google.inject.TypeLiteral
import com.google.inject.name.Names
import org.ktorm.database.Database

class InkerBotModule : JvmPlugin {
    override fun configure(binder: Binder) {
        binder.bind(EventContextKey.Builder::class.java).to(InkEventContextKey.Builder::class.java)
        binder.bind(EventManager::class.java).to(InkEventManager::class.java)

        binder.bind(AtComponent.Factory::class.java).to(InkAt.Factory::class.java)
        binder.bind(MuiltComponent.Factory::class.java).to(InkMuilt.Factory::class.java)
        binder.bind(PlainTextComponent.Factory::class.java).to(InkPlainText.Factory::class.java)
        binder.bind(MuiltComponent.Builder::class.java).to(InkMuilt.Builder::class.java)

        binder.bind(PluginDepend.Builder::class.java).to(InkPluginDepend.Builder::class.java)
        binder.bind(PluginManager::class.java).to(InkPluginManager::class.java)
        binder.bind(PluginMeta.Builder::class.java).to(InkPluginMeta.Builder::class.java)
        binder.bind(PluginUrls.Builder::class.java).to(InkPluginUrls.Builder::class.java)

        binder.bind(ResourceKey.Factory::class.java).to(InkResourceKey.Factory::class.java)
        binder.bind(Identity.Factory::class.java).to(InkIdentity.Factory::class.java)

        binder.bind(Registry.Factory::class.java).to(InkRegistry.Factory::class.java)

        binder.bind(ConfigService.Factory::class.java).to(InkConfigService.Factory::class.java)
        binder.bind(InkSetting::class.java).toProvider(ImplSettingProvider::class.java)

        binder.bind(Frame::class.java).to(InkFrame::class.java)
        binder.bind(ServiceManager::class.java).to(InkServiceManager::class.java)
        binder.bind(DatabaseService::class.java).to(InkDatabaseService::class.java)
        binder.bind(Database::class.java).toProvider(DatabaseService::class.java)
    }

    @EventHandler
    fun onRegisterService(e:LifeStyleEvent.RegisterService) {
        e.binder.bind(DatabaseService::class.java).annotatedWith(Names.named("h2")).to(H2DatabaseService::class.java)
        // e.binder.bind(object :TypeLiteral<Registrar<String>>() {}).annotatedWith(Names.named("a:b")).to(InkRegistryKey::class.java)
    }
}