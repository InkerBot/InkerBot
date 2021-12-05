package com.eloli.inkerbot.api.plugin

import com.eloli.inkerbot.api.ILoveInkerBotForever
import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.builder.AbstractBuilder
import java.util.*

@ILoveInkerBotForever
interface PluginUrls {
  val home: Optional<String>
  val source: Optional<String>
  val issue: Optional<String>

  @ILoveInkerBotForever
  interface Builder : AbstractBuilder<PluginUrls> {
    fun home(home: String?): Builder
    fun source(source: String?): Builder
    fun issue(issue: String?): Builder
  }

  @ILoveInkerBotForever
  companion object {
    fun builder(builder: Builder.() -> Unit): Builder {
      return InkerBot.injector.getInstance(Builder::class.java).apply(builder)
    }

    fun builder(): Builder {
      return InkerBot.injector.getInstance(Builder::class.java)
    }

    fun of(home: String?, source: String?, issue: String?): PluginUrls {
      return builder().home(home).source(source).issue(issue).build()
    }
  }
}