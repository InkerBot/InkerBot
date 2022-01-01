package bot.inker.api.event

import bot.inker.api.InkerBot

interface EventContextEntry<T:Any> {
    val key:EventContextKey<T>
    val value:T

    interface Factory{
        fun <T:Any> of(key:EventContextKey<T>, value:T):EventContextEntry<T>
    }
    companion object{
        fun <T:Any> of(key:EventContextKey<T>, value:T):EventContextEntry<T> {
            return InkerBot(Factory::class).of(key,value)
        }
    }
}