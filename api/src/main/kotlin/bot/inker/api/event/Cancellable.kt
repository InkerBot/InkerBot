package bot.inker.api.event

interface Cancellable : Event {
  var cancelled: Boolean
  fun cancel() {
    cancelled = true
  }
}