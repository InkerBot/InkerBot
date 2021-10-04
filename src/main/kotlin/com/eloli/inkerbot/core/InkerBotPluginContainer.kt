package com.eloli.inkerbot.core

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.plugin.PluginContainer
import com.eloli.inkerbot.api.plugin.PluginMeta
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Path
import javax.inject.Singleton

@Singleton
class InkerBotPluginContainer : PluginContainer {
    override val name: String = "inkerbot"
    override val meta: PluginMeta = PluginMeta.builder {
        name("inkerbot")
        describe("inkerbot core plugin")
        version("1.0-SNAPSHOT")
        urls {
            home("https://github.com/InkerBot")
            source("https://github.com/InkerBot/InkerBot")
            issue("https://github.com/InkerBot/InkerBot/issues")
        }
        main("com.eloli.inkerbot.core.InkerBotModule")
    }.build()
    override val loader: ClassLoader = InkerBot::class.java.classLoader
    override val logger: Logger = LoggerFactory.getLogger("plugin@inkerbot")
    override val dataPath: Path = File("./storage/inkerbot").toPath()
    override val configPath: Path = File("./config/inkerbot").toPath()
    override val enabled: Boolean = true

    override fun addDepend(depend: PluginContainer) {
        //
    }

    override fun load() {
        //
    }

    override fun enable() {
        //
    }

    override fun disable() {
        //
    }


    override fun toString(): String {
        return "MemPlugin@inkerbot"
    }
}