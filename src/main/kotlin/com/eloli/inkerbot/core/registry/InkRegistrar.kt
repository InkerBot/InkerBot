package com.eloli.inkerbot.core.registry

import com.eloli.inkerbot.api.registry.Registrar
import com.eloli.inkerbot.api.registry.UpdatableRegistrar
import com.eloli.inkerbot.api.service.DatabaseService
import com.eloli.inkerbot.api.util.Identity
import org.apache.commons.beanutils.BeanUtils
import org.checkerframework.checker.nullness.Opt
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.HashMap

/**
 * T: RegistryClass
 * U: RealClass with U(V)
 * V: RecordClass
 */
open class InkRegistrar<T,U:T,V>(
    val realClass: Class<U>,
    val recordClass: Class<V>
    ):UpdatableRegistrar<T,U,V> {
    @Inject
    private lateinit var databaseService: DatabaseService
    private val storage = HashMap<Identity,Pair<U,V>>()

    override fun get(identity: Identity): Optional<T> {
        return Optional.ofNullable(storage.getOrElse(identity){
            val session = databaseService.session
            val record = session.get(recordClass,identity.uuid)
            return@getOrElse Pair(
                if (record == null){ null }else{ newInstance(record) },
                record
            )
        }.first)
    }

    override fun update(identity: Identity, command: (V)->Unit):U {
        val session = databaseService.session
        var record = Optional.ofNullable(storage[identity])
                .map { it.second }.orElse(null)
        if(record == null){
            record = session.get(recordClass,identity.uuid)
        }
        if(record == null){
            record = recordClass.getConstructor().newInstance().apply(command)
            session.beginTransaction()
            session.save(record)
            session.transaction.commit()
            val instance = newInstance(record)
            storage[identity]= Pair(instance,record)
            return instance
        }else{
            var sourceRecord:V? = null
            try {
                sourceRecord = BeanUtils.cloneBean(record) as V
            }catch (e:Exception){

            }
            record.apply(command)
            if(record != sourceRecord){
                session.beginTransaction()
                session.update(record)
                session.transaction.commit()
            }
            return Optional.ofNullable(storage[identity]).map { it.first }.orElseGet {
                newInstance(record)
            }
        }
    }

    protected open fun newInstance(record:V):U {
        return realClass.getConstructor(recordClass).newInstance(record)
    }

    @Singleton
    class Factory:UpdatableRegistrar.Factory{
        override fun <T, U : T, V> of(
            registrarClass: Class<T>,
            realClass: Class<U>,
            recordClass: Class<V>
        ): UpdatableRegistrar<T, U, V> {
            return InkRegistrar(realClass,recordClass)
        }
    }
}