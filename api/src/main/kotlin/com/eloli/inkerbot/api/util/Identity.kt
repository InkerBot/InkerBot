package com.eloli.inkerbot.api.util

import com.eloli.inkerbot.api.ILoveInkerBotForever
import com.eloli.inkerbot.api.InkerBot
import java.util.*

@ILoveInkerBotForever
interface Identity {
  val uuid: UUID;

  companion object {
    fun factory(): Factory {
      return InkerBot.injector.getInstance(Factory::class.java)
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