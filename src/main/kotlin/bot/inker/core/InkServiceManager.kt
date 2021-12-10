package bot.inker.core

import bot.inker.api.ServiceManager
import bot.inker.api.event.EventManager
import bot.inker.core.event.lifestyle.InkLifecycleEvent
import bot.inker.core.util.ProxyInjector
import com.google.inject.Binder
import com.google.inject.Module
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InkServiceManager : ProxyInjector(), ServiceManager {
  override var inited: Boolean = false

  @Inject
  private lateinit var eventManager: EventManager
  fun init() {
    registerProxyInjector(
      bot.inker.api.InkerBot.injector.createChildInjector(object : Module {
        override fun configure(binder: Binder) {
          eventManager.post(InkLifecycleEvent.RegisterService(binder))
        }
      })
    )
  }
}