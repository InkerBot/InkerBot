package com.eloli.inkerbot.core

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.core.utils.StaticEntryUtil
import com.google.inject.Guice
import org.apache.log4j.BasicConfigurator

fun main(args: Array<String>) {
    BasicConfigurator.configure()
    val injector = Guice.createInjector(
        InkerBotModule()
    )
    StaticEntryUtil.applyInjector(InkerBot::class.java.classLoader, injector)
    injector.getInstance(InkerBot::class.java)
}