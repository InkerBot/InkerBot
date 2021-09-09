package com.eloli.inkerbot.api.plugin

import org.slf4j.Logger
import java.lang.Exception
import java.nio.file.Path

interface PluginContainer {
    val name: String
    val meta: PluginMeta
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