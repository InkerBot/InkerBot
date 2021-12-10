package bot.inker.api.plugin

import bot.inker.api.builder.AbstractBuilder
import java.util.*

interface PluginUrls {
  val home: Optional<String>
  val source: Optional<String>
  val issue: Optional<String>

  interface Builder : AbstractBuilder<PluginUrls> {
    fun home(home: String?): Builder
    fun source(source: String?): Builder
    fun issue(issue: String?): Builder
  }

  companion object {
    fun builder(builder: Builder.() -> Unit): Builder {
      return bot.inker.api.InkerBot.injector.getInstance(Builder::class.java).apply(builder)
    }

    fun builder(): Builder {
      return bot.inker.api.InkerBot.injector.getInstance(Builder::class.java)
    }

    fun of(home: String?, source: String?, issue: String?): PluginUrls {
      return builder().home(home).source(source).issue(issue).build()
    }
  }
}