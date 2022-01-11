package bot.inker.api.tasker

interface SchedulerJob {
    fun register()
    fun cancel()
}