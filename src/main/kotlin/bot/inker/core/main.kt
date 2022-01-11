package bot.inker.core

import bot.inker.api.InkerBot
import bot.inker.core.util.StaticEntryUtil
import com.google.inject.Guice
import org.slf4j.LoggerFactory


fun main() {
  val printStream = configureLogger()
  val inkerBotModule = InkerBotModule()
  val injector = Guice.createInjector(
    inkerBotModule,{binder->
      binder.bind(InkConsoleStream::class.java).toInstance(printStream)
    }
  )
  StaticEntryUtil.applyInjector(InkerBot::class.java.classLoader, injector)
  InkerBot(InkFrame::class).start()
}

fun configureLogger():InkConsoleStream{
  val systemStdout = System.out
  val systemStderr = System.err
  val inkConsoleStream = InkConsoleStream(systemStdout,systemStderr) { systemStdout.print(it) }
  System.setOut(inkConsoleStream)
  val stdoutLogger = LoggerFactory.getLogger("stdout")
  val stderrLogger = LoggerFactory.getLogger("stderr")
  inkConsoleStream.initLogger(stdoutLogger,stderrLogger)
  System.setOut(inkConsoleStream.logout)
  System.setErr(inkConsoleStream.logerr)
  BannerPrinter.print(inkConsoleStream.console)
  return inkConsoleStream
}