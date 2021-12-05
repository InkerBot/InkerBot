package com.eloli.inkerbot.core.service

import com.eloli.inkerbot.api.Frame
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.plugin.PluginManager
import com.eloli.inkerbot.api.service.DatabaseService
import com.eloli.inkerbot.core.event.lifestyle.InkLifecycleEvent
import org.h2.Driver
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl
import org.hibernate.boot.registry.classloading.internal.TcclLookupPrecedence
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService
import org.hibernate.cfg.Configuration
import org.hibernate.dialect.H2Dialect
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class H2DatabaseService : DatabaseService {
  @Inject
  private lateinit var frame: Frame

  @Inject
  private lateinit var eventManager: EventManager

  @Inject
  private lateinit var pluginManager: PluginManager
  private var sessionFactory: SessionFactory? = null
  private val logger: Logger = LoggerFactory.getLogger("database@h2")
  override val session: Session
    get() {
      if (sessionFactory == null) {
        val configuration = Configuration()
        configuration.setProperty(
          "hibernate.connection.driver_class",
          Driver::class.java.name
        )
        configuration.setProperty(
          "hibernate.connection.url",
          "jdbc:h2:" + frame.self.dataPath.resolve("storage").toFile()
        )
        configuration.setProperty(
          "hibernate.connection.autocommit",
          "true"
        )
        configuration.setProperty(
          "hibernate.dialect",
          H2Dialect::class.java.name
        )
        configuration.setProperty("hibernate.hbm2ddl.auto", "update")
        eventManager.post(InkLifecycleEvent.RegisterEntity {
          configuration.addAnnotatedClass(it)
        })
        val serviceRegistry = StandardServiceRegistryBuilder()
          .applySettings(configuration.properties)
          .addService(
            ClassLoaderService::class.java, ClassLoaderServiceImpl(
              ArrayList<ClassLoader>().apply {
                add(frame.classLoader)
                addAll(pluginManager.plugins.map { it.loader })
              }, TcclLookupPrecedence.AFTER
            )
          )
          .build()
        sessionFactory = configuration.buildSessionFactory(serviceRegistry)
      }
      return sessionFactory!!.openSession()
    }
}