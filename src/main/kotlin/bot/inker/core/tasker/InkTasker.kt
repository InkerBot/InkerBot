package bot.inker.core.tasker

import bot.inker.api.tasker.Tasker
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Singleton

@Singleton
class InkTasker:Tasker {
    init {
        if (INSTANCE != null) {
            throw IllegalStateException("INKTASKER CAN BE INSTANCED ONLY ONCE")
        }
    }

    val threadPoolExecutor = ScheduledThreadPoolExecutor(
        Runtime.getRuntime().availableProcessors()+2,
        ThreadFactory {
            val id = AtomicInteger()
            Thread(it).apply {
                name = "InkerBot-Async-${id.incrementAndGet()}"
            }
        }
    )

    override fun <T> execute(command:()->T):CompletableFuture<T>{
        return CompletableFuture.supplyAsync(command,this)
    }

    override fun execute(command: Runnable) {
        threadPoolExecutor.execute(command)
    }
    companion object{
        private var INSTANCE:InkTasker? = null
    }
}