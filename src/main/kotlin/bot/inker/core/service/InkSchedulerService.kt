package bot.inker.core.service

import bot.inker.api.event.AutoComponent
import bot.inker.api.event.EventHandler
import bot.inker.api.event.Order
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.service.SchedulerService
import bot.inker.core.event.lifestyle.InkLifecycleEvent
import org.quartz.impl.StdSchedulerFactory
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Singleton

@Singleton
@AutoComponent
class InkSchedulerService: SchedulerService {
    private val schedulerFactory by lazy{ StdSchedulerFactory() }
    override val scheduler by lazy { StdSchedulerFactory().getScheduler() }
    private val cronId = AtomicInteger()

    @EventHandler(order = Order.PRE)
    fun post(event: InkLifecycleEvent.Initialization){
        scheduler.start()
    }

    fun allocId():String{
        return cronId.incrementAndGet().toString()
    }

    @EventHandler
    fun stop(event: LifecycleEvent.ServerStopped){
        for (scheduler in schedulerFactory.allSchedulers) {
            scheduler.shutdown()
        }
    }
}