package bot.inker.core.tasker

import bot.inker.api.tasker.CronSchedulerJob
import org.quartz.CronScheduleBuilder
import org.quartz.ScheduleBuilder
import javax.inject.Singleton

class InkCronSchedulerJob(
    override val express:String,
    action:Runnable
) : InkSchedulerJob(action), CronSchedulerJob {
    override val scheduler: ScheduleBuilder<*> = CronScheduleBuilder.cronSchedule(express)
    @Singleton
    class Factory:CronSchedulerJob.Factory{
        override fun of(express: String, action: Runnable): CronSchedulerJob {
            return InkCronSchedulerJob(express,action)
        }
    }
}