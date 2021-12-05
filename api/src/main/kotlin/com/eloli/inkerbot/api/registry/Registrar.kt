package com.eloli.inkerbot.api.registry

import com.eloli.inkerbot.api.util.Identity
import java.util.*

interface Registrar<T> {
  fun get(identity: Identity): Optional<T>
}