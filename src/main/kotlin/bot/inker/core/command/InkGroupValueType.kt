package bot.inker.core.command

import bot.inker.api.command.GroupValueType
import bot.inker.api.command.MemberValueType
import bot.inker.api.command.UUIDValueType
import bot.inker.api.model.Group
import bot.inker.api.model.Member
import bot.inker.api.registry.Registries
import bot.inker.api.util.Identity
import com.eloli.inkcmd.StringReader
import com.eloli.inkcmd.exceptions.DynamicCommandExceptionType
import javax.inject.Singleton

class InkGroupValueType:GroupValueType{
    private val uuidValueType = UUIDValueType.of()
    override fun parse(reader: StringReader): Group {
        val uuid = uuidValueType.parse(reader)
        return Registries.group.get(Identity.of(uuid)).orElseThrow{
            UNKNOWN_GROUP.createWithContext(reader,uuid)
        }
    }
    companion object{
        val UNKNOWN_GROUP = DynamicCommandExceptionType{ uuid->
            "Unregistered group with uuid $uuid"
        }
    }
    @Singleton
    class Factory:GroupValueType.Factory{
        override fun of(): GroupValueType {
            return InkGroupValueType()
        }
    }
}