package bot.inker.core.service

import bot.inker.api.InkerBot
import bot.inker.api.event.AutoComponent
import bot.inker.api.event.EventHandler
import bot.inker.api.event.Order
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.service.DatabaseService
import bot.inker.core.event.lifestyle.InkLifecycleEvent
import bot.inker.core.setting.InkSetting
import com.google.inject.Key
import com.google.inject.name.Names
import org.hibernate.Session
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@AutoComponent
class InkDatabaseService : DatabaseService {
  @Inject
  private lateinit var setting: InkSetting
  private lateinit var database: DatabaseService
  override val session: Session
    get() = database.session

  override val entities = ArrayList<Class<*>>()

  @EventHandler(order = Order.PRE)
  fun pre(event:InkLifecycleEvent.Initialization){
    event.registerEntity = {
      entities.add(it)
    }
  }

  @EventHandler(order = Order.POST)
  fun post(event:InkLifecycleEvent.Initialization){
    database = InkerBot.serviceManager.getInstance(
      Key.get(DatabaseService::class.java).withAnnotation(Names.named(setting.database))
    )
    database.session.close()
  }
}