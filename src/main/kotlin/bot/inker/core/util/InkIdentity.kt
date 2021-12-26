package bot.inker.core.util

import bot.inker.api.util.Identity
import java.util.*

class InkIdentity private constructor(override val uuid: UUID) : Identity {
  override fun hashCode(): Int {
    return uuid.hashCode()
  }

  override fun equals(other: Any?): Boolean {
    return uuid.equals(other)
  }

  override fun toString(): String {
    return uuid.toString()
  }

  class Factory : Identity.Factory {
    override fun of(uuid: UUID): Identity {
      return InkIdentity(uuid)
    }

    override fun of(name: String): Identity {
      return of(UUID.nameUUIDFromBytes(name.toByteArray(Charsets.UTF_8)))
    }

  }
}