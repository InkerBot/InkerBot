package bot.inker.core.command

import bot.inker.api.InkerBot
import bot.inker.api.command.PermissionPredicate
import bot.inker.api.event.message.MessageEvent
import bot.inker.api.service.PermissionService
import javax.inject.Singleton

class InkPermissionPredicate(
    private val permission:String
):PermissionPredicate {
    private val permissionService:PermissionService by lazy {
        InkerBot.serviceManager.getInstance(PermissionService::class.java)
    }

    override fun test(event: MessageEvent): Boolean {
        return permissionService.hasPermission(event,permission)
    }

    @Singleton
    class Factory:PermissionPredicate.Factory{
        override fun of(permission: String): PermissionPredicate {
            return InkPermissionPredicate(permission)
        }
    }
}