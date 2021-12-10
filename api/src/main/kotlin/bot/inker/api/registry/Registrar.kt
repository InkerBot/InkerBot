package bot.inker.api.registry

import bot.inker.api.util.Identity
import java.util.*

interface Registrar<T> {
  fun get(identity: Identity): Optional<T>
}