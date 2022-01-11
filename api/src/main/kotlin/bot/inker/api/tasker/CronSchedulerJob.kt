package bot.inker.api.tasker

import bot.inker.api.InkerBot

interface CronSchedulerJob :SchedulerJob{
    val express:String

    interface Factory{
        fun of(express:String,action:Runnable):CronSchedulerJob
    }
    companion object{
        private val factory:Factory by InkerBot.lazy()
        fun of(express:String,action:Runnable):CronSchedulerJob{
            return factory.of(express,action)
        }
    }
}