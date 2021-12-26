package com.eloli.inkerbot.mirai.command

import bot.inker.api.event.message.MessageEvent
import com.eloli.inkcmd.ktdsl.LiteralArgumentBuilderDsl
import com.eloli.inkcmd.values.BoolValueType
import com.eloli.inkerbot.mirai.command.config.ConfigGroupCommand
import com.eloli.inkerbot.mirai.command.config.ConfigPasswordCommand
import com.eloli.inkerbot.mirai.command.config.ConfigUsernameCommand

object ConfigCommand{
  operator fun invoke(it: LiteralArgumentBuilderDsl<MessageEvent>) {
    it.apply {
      literal("config"){
        describe = "配置 Mirai 所用的用户名，密码"
        option("login-now", BoolValueType.bool()){
          describe("在配置完成后立即应用")
          defaultValue(false)
          defineValue(true)
        }
        ConfigUsernameCommand(this)
        ConfigPasswordCommand(this)
        ConfigGroupCommand(this)
      }
    }
  }
}