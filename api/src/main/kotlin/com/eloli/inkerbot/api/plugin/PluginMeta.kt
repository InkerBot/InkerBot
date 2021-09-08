package com.eloli.inkerbot.api.plugin

import com.eloli.inkerbot.core.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.builder.AbstractBuilder

interface PluginMeta {
    val name:String
    val describe: String
    val version: String
    val urls: PluginUrls
    val main: String
    val depends: Collection<PluginDepend>

    interface Builder : AbstractBuilder<PluginMeta> {
        fun name(name:String): Builder
        fun describe(describe: String?): Builder
        fun version(version: String?): Builder
        fun urls(urls: PluginUrls?): Builder
        fun main(main: String?): Builder
        fun depend(depend: PluginDepend?): Builder
        fun urls(urlsBuilder: PluginUrls.Builder): Builder {
            return this.urls(urlsBuilder.build())
        }

        fun depend(dependBuilder: PluginDepend.Builder): Builder? {
            return this.depend(dependBuilder.build())
        }
    }

    companion object {
        fun builder(): Builder {
            return InkerBot.getInjector().getInstance(Builder::class.java)
        }
    }
}