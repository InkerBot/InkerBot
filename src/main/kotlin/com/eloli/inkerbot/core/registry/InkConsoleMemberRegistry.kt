package com.eloli.inkerbot.core.registry

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.model.ConsoleSender
import com.eloli.inkerbot.api.model.Member
import com.eloli.inkerbot.api.registry.Registrar
import com.eloli.inkerbot.api.util.Identity
import java.util.*
import javax.inject.Singleton

@Singleton
class InkConsoleMemberRegistry : Registrar<Member> {
  val console: ConsoleSender = InkerBot()
  override fun get(identity: Identity): Optional<Member> {
    if (identity != console.identity) {
      return Optional.empty()
    }
    return Optional.of(console);
  }
}