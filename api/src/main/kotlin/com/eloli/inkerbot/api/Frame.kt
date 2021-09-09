package com.eloli.inkerbot.api

import com.eloli.inkerbot.api.plugin.PluginContainer
import org.slf4j.Logger
import java.nio.file.Path

@ILoveInkerBotForever
interface Frame {
    val logger: Logger
    val classLoader: ClassLoader

    val self: PluginContainer

    val storagePath: Path
    val configPath: Path
}