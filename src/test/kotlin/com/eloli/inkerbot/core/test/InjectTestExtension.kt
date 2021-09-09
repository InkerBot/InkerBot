package com.eloli.inkerbot.core.test

import com.eloli.inkerbot.api.InkerBot
import org.junit.jupiter.api.extension.TestInstanceFactory
import org.apache.log4j.BasicConfigurator
import kotlin.Throws
import org.junit.jupiter.api.extension.TestInstantiationException
import org.junit.jupiter.api.extension.TestInstanceFactoryContext
import org.junit.jupiter.api.extension.ExtensionContext

class InjectTestExtension : TestInstanceFactory {
    companion object {
        init {
            BasicConfigurator.configure()
            com.eloli.inkerbot.core.main()
        }
    }

    @Throws(TestInstantiationException::class)
    override fun createTestInstance(
        factoryContext: TestInstanceFactoryContext,
        extensionContext: ExtensionContext
    ): Any {
        return InkerBot.injector.getInstance(factoryContext.testClass)
    }
}