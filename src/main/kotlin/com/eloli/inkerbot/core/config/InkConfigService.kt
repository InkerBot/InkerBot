package com.eloli.inkerbot.core.config

import com.eloli.inkerbot.api.config.ConfigService
import com.eloli.inkerbot.api.plugin.PluginContainer
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.DumperOptions.FlowStyle
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor
import org.yaml.snakeyaml.resolver.Resolver
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Singleton

class InkConfigService<T>(private val path: Path, private val configClass: Class<T>) : ConfigService<T> {
    private val yaml: Yaml
    @Throws(Exception::class)
    override fun load(): T {
        if (!path.toFile().exists()) {
            save(configClass.getConstructor().newInstance())
        }
        return yaml.load(FileReader(path.toFile()))
    }

    @Throws(Exception::class)
    override fun save(obj: T) {
        Files.createDirectories(path.parent)
        FileWriter(path.toFile()).use { fileWriter -> yaml.dump(obj, fileWriter) }
    }

    @Singleton
    class Factory : ConfigService.Factory {
        override fun <T> of(pluginContainer: PluginContainer, name: String, configClass: Class<T>): ConfigService<T> {
            return InkConfigService(pluginContainer.configPath.resolve("$name.yml"), configClass)
        }
    }

    init {
        val dumperOptions = DumperOptions()
        dumperOptions.isProcessComments = true
        dumperOptions.defaultFlowStyle = FlowStyle.AUTO
        yaml = Yaml(
            CustomClassLoaderConstructor(configClass, configClass.classLoader),
            InkYamlRepresenter(),
            dumperOptions,
            LoaderOptions(),
            Resolver()
        )
    }
}