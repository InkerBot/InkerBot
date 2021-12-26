package bot.inker.core.registry

import bot.inker.api.registry.Registry
import bot.inker.api.registry.UpdatableRegistrar
import bot.inker.api.service.DatabaseService
import bot.inker.api.util.Identity
import bot.inker.api.util.ResourceKey
import org.apache.http.client.utils.CloneUtils
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.HashMap

/**
 * T: RegistryClass
 * U: RealClass with U(V)
 * V: RecordClass
 */
open class InkRegistrar<T, U : T, V : Cloneable>(
  val registry: Registry<T>,
  val key: ResourceKey,
  val realClass: Class<U>,
  val recordClass: Class<V>
) : UpdatableRegistrar<T, U, V> {

  @Inject
  private lateinit var databaseService: DatabaseService
  private val storage = ConcurrentHashMap<Identity, Pair<U, V>>()

  override fun get(identity: Identity): Optional<T> {
    return Optional.ofNullable(storage.getOrElse(identity) {
      val session = databaseService.session
      val record = session.get(recordClass, identity.uuid)
      return@getOrElse Pair(
        if (record == null) {
          null
        } else {
          newInstance(record)
        },
        record
      )
    }.first)
  }

  override fun update(identity: Identity, command: (V) -> Unit): U {
    val session = databaseService.session
    var record = Optional.ofNullable(storage[identity])
      .map { it.second }.orElse(null)
    if (record == null) {
      record = session.get(recordClass, identity.uuid)
    }
    if (record == null) {
      record = recordClass.getConstructor().newInstance().apply(command)
      session.beginTransaction()
      session.save(record)
      session.transaction.commit()
      registry.bind(identity, key);
      val instance = newInstance(record)
      storage[identity] = Pair(instance, record)
      return instance
    } else {
      var sourceRecord: V? = null
      try {
        sourceRecord = CloneUtils.cloneObject(record)
      } catch (e: Exception) {
        //
      }
      record.apply(command)
      if (record != sourceRecord) {
        session.beginTransaction()
        session.update(record)
        session.transaction.commit()
      }

      if (storage[identity] == null) {
        storage[identity] = Pair(newInstance(record), record)
      }
      return storage[identity]!!.first
    }
  }

  protected open fun newInstance(record: V): U {
    return realClass.getConstructor(recordClass).newInstance(record)
  }

  @Singleton
  class Factory : UpdatableRegistrar.Factory {
    override fun <T, U : T, V : Cloneable> of(
      registry: Registry<T>,
      key: ResourceKey,
      registrarClass: Class<T>,
      realClass: Class<U>,
      recordClass: Class<V>
    ): UpdatableRegistrar<T, U, V> {
      return InkRegistrar(registry,key , realClass, recordClass)
    }
  }
}