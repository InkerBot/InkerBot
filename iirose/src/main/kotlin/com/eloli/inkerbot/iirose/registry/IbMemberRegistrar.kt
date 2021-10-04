package com.eloli.inkerbot.iirose.registry

import com.eloli.inkerbot.api.model.Member
import com.eloli.inkerbot.api.registry.Registrar
import com.eloli.inkerbot.api.service.DatabaseService
import com.eloli.inkerbot.api.util.Identity
import com.eloli.inkerbot.iirose.model.IbGroup
import com.eloli.inkerbot.iirose.model.IbMember
import java.util.*
import javax.annotation.processing.Generated
import javax.inject.Inject
import javax.inject.Singleton
import javax.persistence.*
import kotlin.collections.HashMap

@Singleton
class IbMemberRegistrar:Registrar<Member> {
    @Inject
    private lateinit var databaseService: DatabaseService
    private val cache:MutableMap<UUID, IbMember> = HashMap()

    override fun get(identity: Identity): Optional<Member> {
        return Optional.ofNullable(cacheOr(identity){
            val session = databaseService.session
            val record = session.get(IbMemberRecord::class.java, identity.uuid)
            if(record == null){
                return@cacheOr null
            }else{
                return@cacheOr IbMember(record.userId, record.lastName)
            }
        })
    }

    fun set(userId:String, name: String):IbMember{
        return updateCache(userId, name) {ibMember->
            val session = databaseService.session
            val record = session.get(IbMemberRecord::class.java, ibMember.identity.uuid)
            if (record == null) {
                session.save(IbMemberRecord().apply{
                    this.userId = userId
                    this.lastName = name
                })
            }else{
                session.save(record.apply{
                    this.lastName = name
                })
            }
        }
    }

    private fun cacheOr(identity: Identity, or:()->IbMember?):IbMember?{
        var cache = this.cache[identity.uuid]
        if(cache == null){
            cache = or.invoke()
            if(cache != null) {
                this.cache[identity.uuid] = cache
            }
        }
        return cache
    }

    private fun updateCache(userId:String, name: String, update:(IbMember)->Unit):IbMember{
        val uuid = Identity.of(userId).uuid
        val cache = this.cache[uuid]
        return if(cache == null){
            val result = IbMember(userId, name)
            update.invoke(result)
            this.cache[uuid] = result
            result
        }else if(cache.name != name){
            cache.name = name
            update.invoke(cache)
            cache
        }else{
            cache
        }
    }

    @Entity
    @Table(name = "ib_member")
    class IbMemberRecord{
        @Id
        @GeneratedValue
        @Column(name = "id")
        lateinit var id:UUID
        @Column(name = "user_id")
        lateinit var userId:String
        @Column(name = "last_name")
        lateinit var lastName:String
    }
}