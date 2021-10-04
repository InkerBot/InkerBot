package com.eloli.inkerbot.mirai.registry

import com.eloli.inkerbot.api.model.Member
import com.eloli.inkerbot.api.registry.Registrar
import com.eloli.inkerbot.api.util.Identity
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.uuid
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MiraiMemberRegistrar : Registrar<Member> {
    @Inject
    private lateinit var database: Database
    override fun get(identity: Identity): Optional<Member> {
        val qqNumber = Optional.ofNullable(database.from(MiraiMember)
            .select(MiraiMember.qqNumber)
            .where { MiraiMember.id eq identity.uuid }
            .limit(1)
            .map { it[MiraiMember.qqNumber] }
            .firstOrNull())
        if (qqNumber.isEmpty) {
            return Optional.empty()
        } else {
            TODO()
        }
    }
    object MiraiMember: Table<Nothing>("mirai_member") {
        val id = uuid("id").primaryKey()
        val qqNumber = long("qq_number")
    }
}