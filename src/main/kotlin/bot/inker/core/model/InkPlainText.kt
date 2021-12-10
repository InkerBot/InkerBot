package bot.inker.core.model

import bot.inker.api.model.message.PlainTextComponent
import javax.inject.Singleton

class InkPlainText private constructor(override val context: String) : PlainTextComponent {

  @Singleton
  class Factory : PlainTextComponent.Factory {
    override fun of(context: String): PlainTextComponent {
      return InkPlainText(context)
    }
  }

  override fun toString(): String {
    return context
  }
}