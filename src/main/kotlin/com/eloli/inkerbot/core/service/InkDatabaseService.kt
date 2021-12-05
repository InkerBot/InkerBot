package com.eloli.inkerbot.core.service

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.service.DatabaseService
import com.eloli.inkerbot.core.setting.InkSetting
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
        database = InkerBot.serviceManager.getInstance(
          Key.get(DatabaseService::class.java).withAnnotation(Names.named(setting.database))
        )
      }
      return database!!.session
    }
}