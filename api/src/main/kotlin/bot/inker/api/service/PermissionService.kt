package bot.inker.api.service

import bot.inker.api.event.message.MessageEvent
import bot.inker.api.model.Member
import bot.inker.api.permission.PermissionResult

interface PermissionService {
    fun getPermission(member:Member,permission: String):PermissionResult
    fun getPermission(event:MessageEvent,permission: String):PermissionResult

    fun hasPermission(member:Member,permission: String):Boolean
    fun hasPermission(event:MessageEvent,permission: String):Boolean
}