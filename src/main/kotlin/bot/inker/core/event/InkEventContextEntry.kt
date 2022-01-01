package bot.inker.core.event

import bot.inker.api.event.EventContextEntry
import bot.inker.api.event.EventContextKey
import javax.inject.Singleton

class InkEventContextEntry<T:Any> private constructor(
    override val key: EventContextKey<T>,
    override val value: T
) :EventContextEntry<T>{
    @Singleton
    class Factory:EventContextEntry.Factory{
        override fun <T : Any> of(key: EventContextKey<T>, value: T): EventContextEntry<T> {
            return InkEventContextEntry(key,value)
        }
    }
}