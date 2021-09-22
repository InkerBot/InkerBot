package com.eloli.inkerbot.mirai.database

import org.ktorm.schema.Table
import org.ktorm.schema.uuid

object MiraiMember : Table<Nothing>("mirai_member") {
    val id = uuid("id").primaryKey()
    val qqNumber = uuid("qq_number").primaryKey()
}