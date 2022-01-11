package bot.inker.api.tasker

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

interface Tasker:Executor {
    fun <T> execute(command:()->T): CompletableFuture<T>
}