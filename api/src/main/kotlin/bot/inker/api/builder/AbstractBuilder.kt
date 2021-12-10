package bot.inker.api.builder

interface AbstractBuilder<T> {
  fun build(): T
}