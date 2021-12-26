package bot.inker.core

import bot.inker.api.InkerBot
import bot.inker.api.ServiceManager
import bot.inker.api.event.AutoComponent
import bot.inker.api.event.EventHandler
import bot.inker.api.event.EventManager
import bot.inker.api.event.Order
import bot.inker.core.event.lifestyle.InkLifecycleEvent
import bot.inker.core.util.ProxyInjector
import com.google.inject.AbstractModule
import com.google.inject.Binder
import com.google.inject.Module
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@AutoComponent
class InkServiceManager : ProxyInjector(), ServiceManager {
  override var inited: Boolean = false

  @Inject
  private lateinit var eventManager: EventManager

  private var actions = ArrayList<Binder.()->Unit>()

  @EventHandler(order = Order.PRE)
  fun pre(event: InkLifecycleEvent.Initialization) {
    event.registerService = {
      actions.add(it)
    }
  }

  @EventHandler(order = Order.BEFORE_POST)
  fun post(event: InkLifecycleEvent.Initialization) {
    registerProxyInjector(
      InkerBot.injector.createChildInjector(
        object :Module{
          override fun configure(binder: Binder) {
            for (action in actions) {
              action.invoke(binder)
            }
          }
        }
      )
    )
  }
}