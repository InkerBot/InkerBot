package com.eloli.inkerbot.iirose.registry

import com.eloli.inkerbot.api.model.Group
import com.eloli.inkerbot.api.registry.Registrar
import com.eloli.inkerbot.api.service.DatabaseService
import com.eloli.inkerbot.api.util.Identity
import com.eloli.inkerbot.iirose.model.IbGroup
import java.util.*
import javax.annotation.processing.Generated
import javax.inject.Inject
import javax.inject.Singleton
import javax.persistence.*

@Singleton
class IbGroupRegistrar:Registrar<Group> {
    @Inject
    private lateinit var databaseService: DatabaseService
    private val cache:MutableMap<UUID, IbGroup> = HashMap()

    override fun get(identity: Identity): Optional<Group> {
        return Optional.ofNullable(cacheOr(identity){
            val session = databaseService.session
            val record = session.get(IbGroupRecord::class.java, identity.uuid)
            if(record == null){
                return@cacheOr null
            }else{
                return@cacheOr IbGroup(record.roomId, record.roomId+"_name", IbGroup.Storage())
            }
        })
    }

    fun set(roomId: String): Group {
        return updateCache(roomId) {ibGroup->
            val session = databaseService.session
            if(session.get(IbGroupRecord::class.java, ibGroup.identity.uuid) == null){
                session.save(IbGroupRecord().apply {
                    this.id = ibGroup.identity.uuid
                    this.roomId = ibGroup.roomId
                })
            }
        }
    }


    private fun cacheOr(identity: Identity, or:()-> IbGroup?): IbGroup?{
        var cache = this.cache[identity.uuid]
        if(cache == null){
            cache = or.invoke()
            if(cache != null) {
                this.cache[identity.uuid] = cache
            }
        }
        return cache
    }

    private fun updateCache(roomId: String, update:(IbGroup)->Unit):IbGroup{
        val uuid = Identity.of(roomId).uuid
        val cache = this.cache[uuid]
        return if(cache == null){
            val result = IbGroup(roomId, roomId+"_room",IbGroup.Storage())
            update.invoke(result)
            this.cache[uuid] = result
            result
        }else{
            cache
        }
    }

    @Entity
    @Table(name = "ib_group")
    class IbGroupRecord{
        @Id
        @GeneratedValue
        @Column(name = "id")
        lateinit var id:UUID

        @Column(name = "roomId")
        lateinit var roomId:String
    }
}