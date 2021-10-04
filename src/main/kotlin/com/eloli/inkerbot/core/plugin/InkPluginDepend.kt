package com.eloli.inkerbot.core.plugin

import com.eloli.inkerbot.api.plugin.PluginDepend
import java.util.*

class InkPluginDepend(override val name: String, override val type: PluginDepend.Type) : PluginDepend {
    class Builder : PluginDepend.Builder {
        var name: String? = null
        var type: PluginDepend.Type? = null
        override fun name(name: String): PluginDepend.Builder {
            this.name = name
            return this
        }

        override fun type(type: PluginDepend.Type): PluginDepend.Builder {
            this.type = type
            return this
        }

        override fun build(): PluginDepend {
            Objects.requireNonNull(name, "name")
            Objects.requireNonNull(name, "name")
            return InkPluginDepend(name!!, type!!)
        }

    }
}