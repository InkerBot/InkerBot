package bot.inker.api.model.message

import bot.inker.api.model.Member

interface AtComponent : MessageComponent {
  val target: Member

  companion object {
    fun factory(): Factory {
      return bot.inker.api.InkerBot.injector.getInstance(Factory::class.java)
    }

    fun of(target: Member): AtComponent {
      return factory().of(target)
    }
  }

  interface Factory {
    fun of(target: Member): AtComponent
  }
}