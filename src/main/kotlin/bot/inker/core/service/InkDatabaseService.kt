package bot.inker.core.service

import bot.inker.api.service.DatabaseService
import bot.inker.core.setting.InkSetting
import com.google.inject.Key
import com.google.inject.name.Names
import org.hibernate.Session
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InkDatabaseService : DatabaseService {
  @Inject
  private lateinit var setting: InkSetting
  private var database: DatabaseService? = null
  override val session: Session
    get() {
      if (database == null) {
        database = bot.inker.api.InkerBot.serviceManager.getInstance(
          Key.get(DatabaseService::class.java).withAnnotation(Names.named(setting.database))
        )
      }
      return database!!.session
    }
}