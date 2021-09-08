package com.eloli.inkerbot.api.plugin

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.builder.AbstractBuilder

interface PluginUrls {
    val home: String?
    val source: String?
    val issue: String?
    interface Builder : AbstractBuilder<PluginUrls> {
        fun home(home: String?): Builder
        fun source(source: String?): Builder
        fun issue(issue: String?): Builder
    }

    companion object {
        fun builder(): Builder {
            return InkerBot.getInjector().getInstance(Builder::class.java)
        }

        fun of(home: String?, source: String?, issue: String?): PluginUrls {
            return builder().home(home).source(source).issue(issue).build()
        }
    }
}