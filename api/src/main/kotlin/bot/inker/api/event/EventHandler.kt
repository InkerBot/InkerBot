package bot.inker.api.event

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class EventHandler(val order: Order = Order.DEFAULT, val beforeModifications: Boolean = false)