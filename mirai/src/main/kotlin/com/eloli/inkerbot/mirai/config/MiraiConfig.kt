package com.eloli.inkerbot.mirai.config

import bot.inker.api.config.Comment

class MiraiConfig {
  @Comment("QQ号")
  var qqNumber:String = ""

  @Comment("密码")
  var qqPassword:String = ""

  @Comment("禁用的群")
  var disabledGroups:Array<String> = Array(0) { "" }
}