package bot.inker.core.registry

import bot.inker.api.model.ConsoleSender
import bot.inker.api.model.Member
import bot.inker.api.registry.Registrar
import bot.inker.api.util.Identity
import java.util.*
import javax.inject.Singleton

@Singleton
class InkConsoleMemberRegistry : Registrar<Member> {
  val console: ConsoleSender = bot.inker.api.InkerBot()
  override fun get(identity: Identity): Optional<Member> {
    if (identity != console.identity) {
      return Optional.empty()
    }
    return Optional.of(console);
  }
}