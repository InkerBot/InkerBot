package com.eloli.inkerbot.core

import com.eloli.inkerbot.api.Frame
import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.plugin.PluginManager
import com.eloli.inkerbot.api.event.lifestyle.LifeStyleEvent
import com.eloli.inkerbot.core.event.lifestyle.InkLifeStyleEvent
import com.eloli.inkerbot.core.setting.InkSetting
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InkFrame:Frame {
    override val logger: Logger
        get() = LoggerFactory.getLogger(InkerBot::class.java)
    override val classLoader: ClassLoader
        get() = InkerBot::class.java.classLoader
    @Inject
    override lateinit var self: InkerBotPluginContainer
    override val storagePath: Path = File("./storage").toPath()
    override val configPath: Path = File("./config").toPath()

    @Inject
    private lateinit var setting: InkSetting
    @Inject
    private lateinit var pluginManager: PluginManager
    @Inject
    private lateinit var eventManager: EventManager

    fun init(){
        if (setting.banner) {
            BannerPrinter.print(System.out)
        }

        pluginManager.addPlugin(self)

        val pluginPath = File("./plugins").toPath()
        Files.createDirectories(pluginPath)
        Arrays.stream(
            Objects.requireNonNull<Array<File>>(pluginPath.toFile().listFiles())
        ).filter { file: File ->
            file.name.endsWith(".jar")
        }.forEach { file: File ->
            pluginManager.addPlugin(file)
        }

        pluginManager.load()
        pluginManager.enable()

        eventManager.post(InkLifeStyleEvent.Enable())
    }
}