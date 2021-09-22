package com.eloli.inkerbot.core.plugin

import com.eloli.inkerbot.api.plugin.PluginDepend
import com.eloli.inkerbot.api.plugin.PluginMeta
import com.eloli.inkerbot.api.plugin.PluginUrls
import java.util.*

class InkPluginMeta private constructor(
    override val name: String,
    override val describe: String,
    override val version: String,
    override val urls: PluginUrls,
    override val main: String,
    override val depends: Collection<PluginDepend>
) : PluginMeta {
    class Builder : PluginMeta.Builder {
        var name: String? = null
        var describe: String? = null
        var version: String? = null
        var urls: PluginUrls? = null
        var main: String? = null
        var depends: MutableCollection<PluginDepend> = ArrayList()
        override fun name(name: String): PluginMeta.Builder {
            this.name = name
            return this
        }

        override fun describe(describe: String): PluginMeta.Builder {
            this.describe = describe
            return this
        }

        override fun version(version: String): PluginMeta.Builder {
            this.version = version
            return this
        }

        override fun urls(urls: PluginUrls): PluginMeta.Builder {
            this.urls = urls
            return this
        }

        override fun main(main: String): PluginMeta.Builder {
            this.main = main
            return this
        }

        override fun depend(depend: PluginDepend): PluginMeta.Builder {
            this.depends.add(depend)
            return this
        }

        override fun build(): PluginMeta {
            Objects.requireNonNull(name, "name")
            Objects.requireNonNull(describe, "describe")
            Objects.requireNonNull(version, "version")
            Objects.requireNonNull(urls, "urls")
            Objects.requireNonNull(main, "main")
            return InkPluginMeta(
                name!!,
                describe!!,
                version!!,
                urls!!,
                main!!,
                depends
            )
        }

    }
}