package bot.inker.api

import com.google.inject.Injector

interface ServiceManager : Injector {
  val inited: Boolean
}