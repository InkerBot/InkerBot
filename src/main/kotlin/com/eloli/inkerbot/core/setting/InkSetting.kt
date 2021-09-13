package com.eloli.inkerbot.core.setting

import com.eloli.inkerbot.api.config.Comment
import com.eloli.inkerbot.api.config.KotlinComment

class InkSetting {
    @Comment("Should print InkerBot's banner when inkerbot start up?")
    var banner = true

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