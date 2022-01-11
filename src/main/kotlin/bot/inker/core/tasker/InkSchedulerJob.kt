package bot.inker.core.tasker

import bot.inker.api.InkerBot
import bot.inker.api.tasker.SchedulerJob
import bot.inker.core.service.InkSchedulerService
import org.quartz.*
import java.util.*
import javax.inject.Inject

abstract class InkSchedulerJob(
    val action: Runnable
):SchedulerJob {
    abstract val scheduler:ScheduleBuilder<*>
    private val schedulerFactory: InkSchedulerService by InkerBot.lazy()
    val job by lazy {
        JobBuilder.newJob()
            .ofType(SchedulerRunableJob::class.java)
            .usingJobData(JobDataMap().apply {
                put(ACTION_KEY_NAME,action)
            })
            .withIdentity(JOB_GROUP, schedulerFactory.allocId())
            .build()
    }
    val trigger by lazy {
        TriggerBuilder.newTrigger()
            .withIdentity(TRIGGER_GROUP,schedulerFactory.allocId())
            .forJob(job)
            .startAt(Date())
            .startNow()
            .withSchedule(scheduler)
            .build()
    }

    override fun register() {
        schedulerFactory.scheduler.scheduleJob(job,trigger)
    }

    override fun cancel() {
        schedulerFactory.scheduler.deleteJob(job.key)
        schedulerFactory.scheduler.pauseTrigger(trigger.key)
    }

    companion object{
        val ACTION_KEY_NAME = "ink-scheduler-factory-action"
        val JOB_GROUP = "ink-scheduler-job"
        val TRIGGER_GROUP = "ink-scheduler-trigger"
    }

    class SchedulerRunableJob: Job {
        override fun execute(context: JobExecutionContext) {
            (context.jobDetail.jobDataMap.get(ACTION_KEY_NAME) as Runnable).run()
        }
    }
}