package bot.inker.core.setting

import bot.inker.api.config.Comment
import bot.inker.api.config.KotlinComment

class InkSetting {
  @Comment("Should print InkerBot's banner when inkerbot start up?")
  var banner = true

  @Comment("Should enable debug output in console?")
  var debug = false

  @KotlinComment(
    "Maven Repo",
    "In China, you should use \"https://maven.aliyun.com/repository/central/\"."
  )
  var mavenRepo = arrayOf(
    "https://repo1.maven.org/maven2/",
    "https://www.jitpack.io/"
  )

  var database = "h2"
}