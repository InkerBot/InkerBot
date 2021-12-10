package bot.inker.api.event

interface EventListener<T : Event> {
  @Throws(Exception::class)
  fun handle(event: T)
}