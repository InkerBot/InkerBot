package bot.inker.api.service

import org.quartz.Scheduler

interface SchedulerService {
    val scheduler:Scheduler
}