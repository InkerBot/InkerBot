package bot.inker.api.command

import bot.inker.api.InkerBot
import bot.inker.api.event.message.MessageEvent
import bot.inker.api.service.PermissionService
import com.eloli.inkcmd.builder.ArgumentBuilder
import com.eloli.inkcmd.ktdsl.ArgumentBuilderDsl
import java.util.function.Predicate

interface PermissionPredicate:Predicate<MessageEvent> {
    companion object{
        fun of(permission:String):PermissionPredicate{
            return InkerBot(Factory::class).of(permission)
        }
    }
    interface Factory{
        fun of(permission:String):PermissionPredicate
    }
}

fun <B : ArgumentBuilder<MessageEvent, B>> ArgumentBuilderDsl<MessageEvent, B>.permission(permission:String):B{
    return this.handler.requires(PermissionPredicate.of(permission))
}