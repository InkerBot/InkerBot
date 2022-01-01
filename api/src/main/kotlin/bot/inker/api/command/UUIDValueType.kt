package bot.inker.api.command

import bot.inker.api.InkerBot
import com.eloli.inkcmd.values.ValueType
import java.util.*

interface UUIDValueType:ValueType<UUID> {
    interface Factory{
        fun of():UUIDValueType
    }
    companion object {
        fun of():UUIDValueType{
            return InkerBot(Factory::class).of()
        }
    }
}