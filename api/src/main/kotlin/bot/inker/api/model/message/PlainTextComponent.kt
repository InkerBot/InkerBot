package bot.inker.api.model.message

interface PlainTextComponent : MessageComponent {
  val context: String

  companion object {
    fun factory(): Factory {
      return bot.inker.api.InkerBot.injector.getInstance(Factory::class.java)
    }

    fun of(context: String): PlainTextComponent {
      return factory().of(context)
    }
  }

  interface Factory {
    fun of(context: String): PlainTextComponent
  }
}