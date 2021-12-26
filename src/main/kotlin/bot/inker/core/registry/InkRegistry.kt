package bot.inker.core.registry

import bot.inker.api.event.AutoComponent
import bot.inker.api.event.EventHandler
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.plugin.PluginManager
import bot.inker.api.registry.Registrar
import bot.inker.api.registry.Registry
import bot.inker.api.service.DatabaseService
import bot.inker.api.util.Identity
import bot.inker.api.util.ResourceKey
import bot.inker.core.InkerBotPluginContainer
import bot.inker.core.plugin.JvmPluginContainer
import com.google.inject.ConfigurationException
import com.google.inject.Key
import com.google.inject.TypeLiteral
import com.google.inject.name.Names
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

class InkRegistry<T>(val type: TypeLiteral<Registrar<T>>) : Registry<T> {
  @Inject
  private lateinit var pluginManager: PluginManager
  @Inject
  private lateinit var databaseService: DatabaseService

  private val map: MutableMap<ResourceKey, Registrar<T>> = HashMap()

  override fun register(key: ResourceKey, registrar: Registrar<T>) {
    if (map.contains(key)) {
      throw IllegalStateException("ResourceKey $key have been register with ${map[key]}.")
    }
    map[key] = registrar
  }

  override fun generate(key: ResourceKey): Identity {
    val uuid = UUID.randomUUID()
    bind(Identity.of(uuid),key)
    return Identity.of(uuid)
  }

  override fun bind(identity:Identity, key: ResourceKey): Unit {
    val uuid = identity.uuid
    val session = databaseService.session
    session.beginTransaction()
    session.save(Record().apply {
      this.resourceKey = key.toString()
      this.uuid = uuid
    })
    session.transaction.commit()
  }

  override fun get(identity: Identity): Optional<T> {
    val session = databaseService.session
    session.beginTransaction()
    val record = session.get(Record::class.java, identity.uuid) ?: return Optional.empty()
    return get(ResourceKey.of(record.resourceKey)).get(identity)
  }

  override fun get(key: ResourceKey): Registrar<T> {
    return map.getOrPut(key) {
      pluginManager.plugins.forEach {
        val injector = when (it) {
          is JvmPluginContainer -> {
            it.injector
          }
          is InkerBotPluginContainer -> {
            bot.inker.api.InkerBot.injector
          }
          else -> {
            return@forEach
          }
        }
        try {
          return@getOrPut bot.inker.api.InkerBot.serviceManager.getInstance(
            Key.get(type).withAnnotation(Names.named(key.toString()))
          )
        } catch (e: ConfigurationException) {
          //
        }
      }
      throw IllegalStateException("Could not found resourceKey $key.")
    }
  }

  @Entity(name = "registry")
  class Record : Cloneable {
    @Id
    @Column
    lateinit var uuid: UUID

    @Column
    lateinit var resourceKey: String
  }

  @AutoComponent
  @Singleton
  class Factory : Registry.Factory {
    private val map: MutableMap<TypeLiteral<Registrar<*>>, Registry<*>> = HashMap()
    override fun <T> of(type: TypeLiteral<Registrar<T>>): Registry<T> {
      return map.getOrPut(type as TypeLiteral<Registrar<*>>) {
        InkRegistry(type as TypeLiteral<Registrar<T>>).apply {
          bot.inker.api.InkerBot.injector.injectMembers(this)
        }
      } as Registry<T>
    }
    @EventHandler
    fun registerEntity(e:LifecycleEvent.RegisterEntity){
      e.register(Record::class.java)
    }
  }
}