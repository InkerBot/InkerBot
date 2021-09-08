package com.eloli.inkerbot.api.plugin

import java.io.File

interface PluginManager {
    val plugins: Collection<PluginContainer>
    fun addPlugin(pluginFile: File)
    fun addPlugin(plugin: PluginContainer)
    fun load()
    fun enable()
}