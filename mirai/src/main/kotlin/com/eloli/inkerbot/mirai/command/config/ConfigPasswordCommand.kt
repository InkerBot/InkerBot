package com.eloli.inkerbot.mirai.command.config

import bot.inker.api.InkerBot
import bot.inker.api.event.message.MessageEvent
import bot.inker.api.model.message.PlainTextComponent
import com.eloli.inkcmd.ktdsl.LiteralArgumentBuilderDsl
import com.eloli.inkcmd.values.StringValueType
import com.eloli.inkerbot.mirai.MiraiCore
import com.eloli.inkerbot.mirai.config.MiraiConfig
import com.eloli.inkerbot.mirai.config.MiraiConfigProvider

object ConfigPasswordCommand {
  operator fun invoke(it: LiteralArgumentBuilderDsl<MessageEvent>) {
    it.apply {
      literal("password"){
        describe = "配置 MIRAI 所用的密码"
        argument("newpassword", StringValueType.string()){
          describe = "配置 MIRAI 所用的密码"
          executes {
            InkerBot(MiraiConfig::class).qqPassword = it.getArgument("newpassword", String::class.java)
            InkerBot(MiraiConfigProvider::class.java).save()
            if(it.getOption("login-now",Boolean::class.java)){
              it.source.sendMessage(
                PlainTextComponent.of("配置完成，立即生效")
              )
              InkerBot(MiraiCore::class).apply {
                close()
                connect()
              }
            }else{
              it.source.sendMessage(
                PlainTextComponent.of("配置完成，使用 \"/mirai login\" 生效")
              )
            }
          }
        }
      }
    }
  }
}