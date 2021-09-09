package com.eloli.inkerbot.api.plugin

import com.eloli.inkerbot.api.ILoveInkerBotForever
import java.io.File

@ILoveInkerBotForever
interface PluginManager {
    val plugins: Collection<PluginContainer>
    fun addPlugin(pluginFile: File)
    fun addPlugin(plugin: PluginContainer)
    fun load()
    fun enable()
}