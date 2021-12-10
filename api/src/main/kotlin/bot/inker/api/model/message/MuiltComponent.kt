package bot.inker.api.model.message

import bot.inker.api.builder.AbstractBuilder

interface MuiltComponent : MessageComponent {
  val subs: List<MessageComponent>

  companion object {
    fun factory(): Factory {
      return bot.inker.api.InkerBot.injector.getInstance(Factory::class.java)
    }

    fun builder(): Builder {
      return bot.inker.api.InkerBot.injector.getInstance(Builder::class.java)
    }

    fun of(subs: List<MessageComponent>): MuiltComponent {
      return factory().of(subs)
    }
  }

  interface Factory {
    fun of(subs: List<MessageComponent>): MuiltComponent
  }

  interface Builder : AbstractBuilder<MuiltComponent> {
    operator fun plus(component: MessageComponent): Builder
    operator fun plusAssign(component: MessageComponent)
    fun add(component: MessageComponent): Builder
  }
}