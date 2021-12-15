package bot.inker.api.util

interface ResourceKey {
  val namespace: String
  val value: String

  interface Factory {
    fun of(namespace: String, value: String): ResourceKey
    fun resolve(formatted: String): ResourceKey
  }

  companion object {
    const val INKERBOT_NAMESPACE = "inkerbot"

    fun inkerbot(value: String): ResourceKey {
      return of(INKERBOT_NAMESPACE, value)
    }

    fun factory(): Factory {
      return bot.inker.api.InkerBot.injector.getInstance(Factory::class.java)
    }

    fun of(namespace: String, value: String): ResourceKey {
      return factory().of(namespace, value)
    }

    fun of(key: String): ResourceKey {
      return factory().resolve(key)
    }
  }
}