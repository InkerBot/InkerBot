package com.eloli.inkerbot.core.plugin

import com.eloli.inkerbot.api.ILoveInkerBotForever
import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.plugin.JvmPlugin
import com.eloli.inkerbot.api.plugin.PluginContainer
import com.eloli.inkerbot.api.plugin.PluginDepend
import com.eloli.inkerbot.api.plugin.PluginMeta
import com.eloli.inkerbot.core.util.ImplPrefixLogger
import com.eloli.inkerbot.core.util.ReadPluginJson
import com.eloli.inkerbot.core.util.StaticEntryUtil
import com.google.gson.Gson
import com.google.inject.Injector
import com.google.inject.Module
import org.slf4j.Logger
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.InvocationTargetException
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Path

class JvmPluginContainer(val jarFile: File) : PluginContainer{
    private val gson: Gson = Gson()
    private lateinit var injector: Injector
    private lateinit var pluginLoader: ClassLoader
    override lateinit var meta: PluginMeta
    override lateinit var logger: Logger

    override val name: String
        get() = meta.name
    override val dataPath: Path
        get() {
            return InkerBot.frame.storagePath.resolve(name)
        }
    override val configPath: Path
        get() {
            return InkerBot.frame.storagePath.resolve(name)
        }

    override var enabled: Boolean = false

    override fun addDepend(depend: PluginContainer) {

    }

    override fun load() {
        this.pluginLoader = URLClassLoader(Array<URL>(1){ jarFile.toURI().toURL() }, InkerBot.frame.classLoader)
        loadMeta()
        for (depend in meta.depends) {
            if (depend.type == PluginDepend.Type.LIBRARY) {
                // this.pluginLoader.addURL(
                //     dependencyResolver.getDependencyFile(depend.name()).toURI().toURL()
                // )
            }
        }
    }

    private fun loadMeta() {
        val metaStream: InputStream = this.pluginLoader.getResourceAsStream("META-INF/plugin.json")
            ?: throw FileNotFoundException("No META-INF/plugin.json found in $jarFile")
        val metaReader = InputStreamReader(metaStream)
        meta = ReadPluginJson.read(metaReader)
        metaReader.close()
        metaStream.close()
    }

    override fun enable() {
        enabled = true
        logger = ImplPrefixLogger(InkerBot.frame.logger, "[$name]: ")
        val mainClass = pluginLoader.loadClass(meta.main)
        val jvmPlugin:JvmPlugin = (mainClass.getConstructor().newInstance() as JvmPlugin)
        if(jvmPlugin::class.java.getAnnotation(ILoveInkerBotForever::class.java) != null){
            logger.error("=".repeat(29))
            logger.error("| InkerBot: I love you too! |")
            logger.error("=".repeat(29))
        }
        injector = InkerBot.injector
            .createChildInjector(Module { binder ->
                binder.bind(PluginContainer::class.java).toInstance(this)
                binder.bind(Logger::class.java).toInstance(logger)
                try {
                    jvmPlugin.configure(binder)
                } catch (e: InstantiationException) {
                    logger.warn("Failed to constructor {}'s Module class {}.", this.name, mainClass.name)
                } catch (e: IllegalAccessException) {
                    logger.warn("Failed to constructor {}'s Module class {}.", this.name, mainClass.name)
                } catch (e: InvocationTargetException) {
                    logger.warn("Failed to constructor {}'s Module class {}.", this.name, mainClass.name)
                } catch (e: NoSuchMethodException) {
                    logger.warn("Failed to constructor {}'s Module class {}.", this.name, mainClass.name)
                }
            })
        StaticEntryUtil.applyInjector(pluginLoader, injector)
        injector.injectMembers(jvmPlugin)
        InkerBot.eventManager.registerListeners(this, jvmPlugin)
    }

    override fun disable() {
        enabled = false
        InkerBot.eventManager.unregisterPluginListeners(this)
    }

    override fun toString(): String {
        return "JvmPlugin@$name"
    }
}