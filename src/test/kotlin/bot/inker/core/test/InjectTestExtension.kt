package bot.inker.core.test

import bot.inker.core.main
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestInstanceFactory
import org.junit.jupiter.api.extension.TestInstanceFactoryContext
import org.junit.jupiter.api.extension.TestInstantiationException

class InjectTestExtension : TestInstanceFactory {
  companion object {
    init {
      main()
    }
  }

  @Throws(TestInstantiationException::class)
  override fun createTestInstance(
    factoryContext: TestInstanceFactoryContext,
    extensionContext: ExtensionContext
  ): Any {
    return bot.inker.api.InkerBot.injector.getInstance(factoryContext.testClass)
  }
}