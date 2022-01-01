package bot.inker.core.event

import bot.inker.api.event.EventContextEntry
import bot.inker.api.event.EventContextKey
import bot.inker.api.util.ResourceKey
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Singleton

class InkEventContextKey<T:Any> private constructor(
    val id:Long,
    override val key: ResourceKey
) :EventContextKey<T> {
    override fun fill(value: T): EventContextEntry<T> {
        return EventContextEntry.of(this,value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InkEventContextKey<*>

        if (id != other.id) return false
        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


    @Singleton
    class Factory:EventContextKey.Factory{
        val atomic = AtomicLong()
        override fun <T:Any> of(key: ResourceKey): EventContextKey<T> {
            return InkEventContextKey(atomic.getAndIncrement(),key)
        }
    }
}