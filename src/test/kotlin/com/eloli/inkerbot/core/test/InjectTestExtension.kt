package com.eloli.inkerbot.core.test

import com.eloli.inkerbot.api.InkerBot
import org.apache.log4j.BasicConfigurator
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestInstanceFactory
import org.junit.jupiter.api.extension.TestInstanceFactoryContext
import org.junit.jupiter.api.extension.TestInstantiationException

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