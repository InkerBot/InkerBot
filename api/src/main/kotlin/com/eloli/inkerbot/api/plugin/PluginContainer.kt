package com.eloli.inkerbot.api.plugin

import com.eloli.inkerbot.api.ILoveInkerBotForever
import org.slf4j.Logger
import java.nio.file.Path

@ILoveInkerBotForever
interface PluginContainer {
    val name: String
    val meta: PluginMeta
    val loader:ClassLoader
    val logger: Logger
    val dataPath: Path
    val configPath: Path
    val enabled: Boolean

    fun addDepend(depend: PluginContainer)

    @Throws(Exception::class)
    fun load()

    @Throws(Exception::class)
    fun enable()

    @Throws(Exception::class)
    fun disable()
}