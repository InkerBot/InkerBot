package com.eloli.inkerbot.api.registry

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.model.Member
import com.google.inject.TypeLiteral

object Registries {
    private val factory:Registry.Factory = InkerBot.injector.getInstance(Registry.Factory::class.java)
    val member: Registry<Member> = factory.of(object :TypeLiteral<Registrar<Member>>(){})
}