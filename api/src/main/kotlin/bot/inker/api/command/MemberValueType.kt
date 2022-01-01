package bot.inker.api.command

import bot.inker.api.InkerBot
import bot.inker.api.model.Member
import com.eloli.inkcmd.values.ValueType

interface MemberValueType:ValueType<Member> {
    interface Factory{
        fun of():MemberValueType
    }
    companion object {
        fun of():MemberValueType{
            return InkerBot(Factory::class).of()
        }
    }
}