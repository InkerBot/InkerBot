package bot.inker.core

import bot.inker.api.event.AutoComponent
import bot.inker.api.event.EventHandler
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.tasker.CronSchedulerJob
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
@AutoComponent
class TestTest {
    val logger = LoggerFactory.getLogger("test-test")
    val job: CronSchedulerJob = CronSchedulerJob.of("0/5 * * * * ? "){
        logger.info("tasted")
    }

    @EventHandler
    fun onEnable(e: LifecycleEvent.ServerStarted) {
        job.register()
    }

    @EventHandler
    fun debug(e:LifecycleEvent.RegisterCommand){
        e.register("debug"){
            executes{
                logger.debug("Hi, here!")
            }
        }
    }
}