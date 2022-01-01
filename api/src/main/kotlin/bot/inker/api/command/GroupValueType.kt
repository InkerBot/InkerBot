package bot.inker.api.command

import bot.inker.api.InkerBot
import bot.inker.api.model.Group
import com.eloli.inkcmd.values.ValueType

interface GroupValueType:ValueType<Group> {
    interface Factory{
        fun of():GroupValueType
    }
    companion object {
        fun of():GroupValueType{
            return InkerBot(Factory::class).of()
        }
    }
}