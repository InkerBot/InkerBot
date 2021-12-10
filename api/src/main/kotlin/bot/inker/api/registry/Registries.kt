package bot.inker.api.registry

import bot.inker.api.model.Group
import bot.inker.api.model.Member
import com.google.inject.TypeLiteral

object Registries {
  private val factory: Registry.Factory = bot.inker.api.InkerBot.injector.getInstance(Registry.Factory::class.java)
  val member: Registry<Member> = factory.of(object : TypeLiteral<Registrar<Member>>() {})
  val group: Registry<Group> = factory.of(object : TypeLiteral<Registrar<Group>>() {})
}