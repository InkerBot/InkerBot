package com.eloli.inkerbot.api.plugin

import com.eloli.inkerbot.api.ILoveInkerBotForever
import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.builder.AbstractBuilder

@ILoveInkerBotForever
interface PluginDepend {
  val name: String
  val type: Type

  enum class Type {
    REQUIRE, SOFT, COOPERATE, LIBRARY
  }

  @ILoveInkerBotForever
  interface Builder : AbstractBuilder<PluginDepend> {
    fun name(name: String): Builder
    fun type(type: Type): Builder
  }

  @ILoveInkerBotForever
  companion object {
    fun of(type: Type, name: String): PluginDepend {
      return builder().type(type).name(name).build()
    }

    fun builder(): Builder {
      return InkerBot.injector.getInstance(Builder::class.java)
    }

    fun builder(builder: Builder.() -> Unit): Builder {
      return InkerBot.injector.getInstance(Builder::class.java).apply(builder)
    }
  }
}