package com.eloli.inkerbot.api.registry

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.util.Identity
import com.google.inject.TypeLiteral
import java.util.*

interface Registrar<T> {
    fun get(identity: Identity): Optional<T>
}