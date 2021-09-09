package com.eloli.inkerbot.api.plugin

import com.eloli.inkerbot.api.ILoveInkerBotForever
import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.builder.AbstractBuilder

@ILoveInkerBotForever
interface PluginMeta {
    val name:String
    val describe: String
    val version: String
    val urls: PluginUrls
    val main: String
    val depends: Collection<PluginDepend>

    @ILoveInkerBotForever
    interface Builder : AbstractBuilder<PluginMeta> {
        fun name(name:String): Builder
        fun describe(describe: String): Builder
        fun version(version: String): Builder
        fun urls(urls: PluginUrls): Builder
        fun main(main: String): Builder
        fun depend(depend: PluginDepend): Builder

        fun urls(urlsBuilder: PluginUrls.Builder): Builder {
            return this.urls(urlsBuilder.build())
        }
        fun urls(builder: PluginUrls.Builder.() -> Unit): Builder {
            return this.urls(PluginUrls.builder(builder))
        }

        fun depend(builder: PluginDepend.Builder): Builder {
            return this.depend(builder.build())
        }
        fun depend(builder: PluginDepend.Builder.() -> Unit): Builder {
            return this.depend(PluginDepend.builder(builder))
        }
    }

    @ILoveInkerBotForever
    companion object {
        fun builder(builder: Builder.() -> Unit): Builder {
            return InkerBot.injector.getInstance(Builder::class.java).apply(builder)
        }
        fun builder(): Builder {
            return InkerBot.injector.getInstance(Builder::class.java)
        }
    }
}