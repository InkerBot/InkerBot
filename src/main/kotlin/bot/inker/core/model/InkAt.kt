package bot.inker.core.model

import bot.inker.api.model.Member
import bot.inker.api.model.message.AtComponent
import javax.inject.Singleton

class InkAt private constructor(override val target: Member) : AtComponent {
  @Singleton
  class Factory : AtComponent.Factory {
    override fun of(target: Member): AtComponent {
      return InkAt(target)
    }
  }
}