package com.eloli.inkerbot.core.plugin

import com.eloli.inkerbot.api.plugin.PluginContainer
import com.eloli.inkerbot.api.plugin.PluginDepend
import com.eloli.inkerbot.api.plugin.PluginManager
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Singleton

@Singleton
class InkPluginManager: PluginManager {
    companion object {
        const val FAILED_TO_LOAD = "Failed to load plugin {}."
    }
    override val plugins: MutableCollection<PluginContainer> = ArrayList()

    private val logger = LoggerFactory.getLogger("plugin-manager")

    override fun addPlugin(pluginFile: File) {
        if (pluginFile.name.endsWith(".jar")) {
            this.addPlugin(JvmPluginContainer(pluginFile))
        }
    }

    override fun addPlugin(plugin: PluginContainer) {
        plugins.add(plugin)
    }

    override fun load() {
        this.load(false)
    }

    fun load(ignoreNotApsaras: Boolean) {
        loadPlugins(ignoreNotApsaras)
        checkPluginsDepends()
        logger.info("Loaded plugin(s): {}.", plugins)
    }

    fun loadPlugins(ignoreNotApsaras: Boolean) {
        val itr = plugins.iterator()
        while (itr.hasNext()) {
            val plugin = itr.next()
            try {
                plugin.load()
            } catch (e: FileNotFoundException) {
                if (!ignoreNotApsaras) {
                    logger.warn(FAILED_TO_LOAD, plugin, e)
                }
                itr.remove()
            } catch (e: Exception) {
                logger.warn(FAILED_TO_LOAD, plugin, e)
                itr.remove()
            }
        }
    }

    fun checkPluginsDepends() {
        val pluginContainerMap: MutableMap<String, PluginContainer> = HashMap()
        for (plugin in plugins) {
            pluginContainerMap[plugin.name] = plugin
        }
        val itr = plugins.iterator()
        while (itr.hasNext()) {
            val plugin = itr.next()
            try {
                checkPluginDepends(pluginContainerMap, plugin)
            } catch (e: Exception) {
                itr.remove()
                logger.warn(FAILED_TO_LOAD, plugin, e)
            }
        }
    }

    fun checkPluginDepends(pluginContainerMap: Map<String, PluginContainer>, plugin: PluginContainer) {
        for (depend in plugin.meta.depends) {
            if (depend.type == PluginDepend.Type.REQUIRE
                || depend.type == PluginDepend.Type.SOFT
                || depend.type == PluginDepend.Type.COOPERATE
            ) {
                val target = pluginContainerMap[depend.name]
                if (depend.type == PluginDepend.Type.SOFT) {
                    continue
                }
                if(target == null){
                    throw NullPointerException(
                        "Can't found " + plugin.name + "'s depend " + depend.name
                    )
                }
                plugin.addDepend(target)
            }
        }
    }

    override fun enable() {
        for (plugin in plugins) {
            try {
                plugin.enable()
            } catch (e: Exception) {
                logger.warn("Failed to enable plugin " + plugin.name + ".", e)
            }
        }
    }

}