package bot.inker.core.event

import bot.inker.api.event.EventContext
import bot.inker.api.event.EventContextEntry
import bot.inker.api.event.EventContextKey
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Stream
import javax.inject.Singleton

class InkEventContext:EventContext {
    val map:MutableMap<EventContextKey<out Any>, Any>
    private constructor(){
        this.map = ConcurrentHashMap<EventContextKey<out Any>, Any>()
    }
    private constructor(map: Map<EventContextKey<out Any>, Any>){
        this.map = ConcurrentHashMap<EventContextKey<out Any>, Any>(map)
    }

    override fun <T:Any> get(key: EventContextKey<T>): Optional<T> {
        return Optional.ofNullable(map.get(key) as T?)
    }

    override fun <T:Any> getOrSave(key: EventContextKey<T>, value:T): T {
        return map.getOrElse(key){ value } as T
    }

    override fun <T:Any> getOrSave(key: EventContextKey<T>, getter: () -> T): T {
        return map.getOrPut(key,getter) as T
    }

    override fun <T:Any> getOrSaveOptional(key: EventContextKey<T>, getter: () -> Optional<T>): Optional<T> {
        return Optional.of(map.getOrPut(key){
            getter().orElse(null)
        } as T)
    }

    override fun <T : Any> getOrSaveNullable(key: EventContextKey<T>, getter: () -> T?): Optional<T> {
        var value = map.get(key)
        if (value == null) {
            value = getter()
        }else{
            return Optional.of(value as T)
        }
        if(value == null){
            return Optional.empty()
        }else{
            return Optional.of(map.put(key,value) as T)
        }
    }

    override fun <T : Any> getOr(key: EventContextKey<T>, value:T): T {
        return map.getOrDefault(key,value) as T
    }

    override fun <T : Any> getOr(key: EventContextKey<T>, getter: () -> T): T {
        return map.getOrElse(key, getter) as T
    }

    override fun <T : Any> getOrOptional(key: EventContextKey<T>, getter: () -> Optional<T>): Optional<T> {
        return Optional.ofNullable(map.getOrElse(key){
            getter().orElse(null)
        } as T?)
    }

    override fun <T : Any> getOrNullable(key: EventContextKey<T>, getter: () -> T?): Optional<T> {
        return Optional.ofNullable(map.getOrElse(key){
            getter()
        } as T?)
    }

    override fun <T:Any> require(key: EventContextKey<T>): T {
        return get(key).get()
    }

    override fun iterator(): Iterator<EventContextEntry<Any>> {
        return map.map { EventContextEntry.of(it.key as EventContextKey<Any>,it.value) }.iterator()
    }

    override fun stream(): Stream<EventContextEntry<Any>> {
        return map.map { EventContextEntry.of(it.key as EventContextKey<Any>,it.value) }.stream()
    }

    override fun clone(): EventContext {
        return InkEventContext(map)
    }

    @Singleton
    class Factory:EventContext.Factory{
        override fun empty(): EventContext = InkEventContext()
        override fun of(vararg values: EventContextEntry<Any>): EventContext {
            val map = HashMap<EventContextKey<out Any>, Any>()
            for (entry in values) {
                map.put(entry.key,entry.value)
            }
            return InkEventContext(map)
        }
    }
}