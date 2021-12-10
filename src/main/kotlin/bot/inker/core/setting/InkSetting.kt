package bot.inker.core.setting

class InkSetting {
  @bot.inker.api.config.Comment("Should print InkerBot's banner when inkerbot start up?")
  var banner = true

  @bot.inker.api.config.Comment("Should enable debug output in console?")
  var debug = false

  @bot.inker.api.config.KotlinComment(
    "Maven Repo",
    "In China, you should use \"https://maven.aliyun.com/repository/central/\"."
  )
  var mavenRepo = arrayOf(
    "https://repo1.maven.org/maven2/",
    "https://www.jitpack.io/"
  )

  var database = "h2"
}