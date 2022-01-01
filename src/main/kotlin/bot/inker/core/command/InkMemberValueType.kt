package bot.inker.core.command

import bot.inker.api.command.GroupValueType
import bot.inker.api.command.MemberValueType
import bot.inker.api.command.UUIDValueType
import bot.inker.api.model.Member
import bot.inker.api.registry.Registries
import bot.inker.api.util.Identity
import com.eloli.inkcmd.StringReader
import com.eloli.inkcmd.exceptions.DynamicCommandExceptionType
import javax.inject.Singleton

class InkMemberValueType:MemberValueType{
    private val uuidValueType = UUIDValueType.of()
    override fun parse(reader: StringReader): Member {
        val uuid = uuidValueType.parse(reader)
        return Registries.member.get(Identity.of(uuid)).orElseThrow{
            UNKNOWN_MEMBER.createWithContext(reader,uuid)
        }
    }
    companion object{
        val UNKNOWN_MEMBER = DynamicCommandExceptionType{ uuid->
            "Unregistered member with uuid $uuid"
        }
    }
    @Singleton
    class Factory: MemberValueType.Factory{
        override fun of(): MemberValueType {
            return InkMemberValueType()
        }
    }
}