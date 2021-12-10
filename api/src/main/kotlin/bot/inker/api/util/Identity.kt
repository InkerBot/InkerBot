package bot.inker.api.util

import java.util.*

interface Identity {
  val uuid: UUID;

  companion object {
    fun factory(): Factory {
      return bot.inker.api.InkerBot.injector.getInstance(Factory::class.java)
    }

    fun of(uuid: UUID): Identity {
      return factory().of(uuid)
    }

    fun of(name: String): Identity {
      return factory().of(name)
    }
  }

  interface Factory {
    fun of(uuid: UUID): Identity
    fun of(name: String): Identity
  }
}