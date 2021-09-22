package com.eloli.inkerbot.core.service

import com.eloli.inkerbot.api.Frame
import com.eloli.inkerbot.api.service.DatabaseService
import org.ktorm.database.Database
import org.ktorm.logging.Slf4jLoggerAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class H2DatabaseService : DatabaseService {
    @Inject
    private lateinit var frame: Frame
    private var database: Database? = null
    private val logger: Logger = LoggerFactory.getLogger("database@h2")
    override fun get(): Database {
        if (database == null) {
            database = Database.connect(
                "jdbc:h2:" + frame.self.dataPath.resolve("storage"),
                driver = "org.h2.Driver",
                logger = Slf4jLoggerAdapter(logger)
            )
            return database as Database
        } else {
            return database as Database
        }
    }
}