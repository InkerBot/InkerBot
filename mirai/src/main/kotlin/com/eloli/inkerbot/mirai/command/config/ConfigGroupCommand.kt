package com.eloli.inkerbot.mirai.command.config

import bot.inker.api.InkerBot
import bot.inker.api.event.message.MessageEvent
import bot.inker.api.model.message.PlainTextComponent
import com.eloli.inkcmd.ktdsl.LiteralArgumentBuilderDsl
import com.eloli.inkcmd.values.StringValueType
import com.eloli.inkerbot.mirai.MiraiCore
import com.eloli.inkerbot.mirai.config.MiraiConfig
import com.eloli.inkerbot.mirai.config.MiraiConfigProvider

object ConfigGroupCommand {
  operator fun invoke(it: LiteralArgumentBuilderDsl<MessageEvent>) {
    it.apply {
      literal("group"){
        describe = "获取 MIRAI 禁用的 Group"
        executes {
          val builder = StringBuilder()
          builder.appendLine("目前禁用的群为：")
          InkerBot(MiraiConfig::class).disabledGroups.forEach {
            builder.appendLine(it)
          }
          it.source.sendMessage(PlainTextComponent.of(builder.toString()))
        }
        literal("add"){
          describe = "添加 MIRAI 禁用的 Group"
          argument("groupNumber", StringValueType.string()){
            describe = "添加 MIRAI 禁用的 Group"
            executes {
              InkerBot(MiraiConfig::class).disabledGroups += it.getArgument("groupNumber",String::class.java)
              InkerBot(MiraiConfigProvider::class.java).save()
              it.source.sendMessage(
                PlainTextComponent.of("已添加")
              )
            }
          }
        }
        literal("remove"){
          describe = "删除 MIRAI 禁用的 Group"
          argument("groupNumber", StringValueType.string()){
            describe = "删除 MIRAI 禁用的 Group"
            executes {
              val groupNumber = it.getArgument("groupNumber",String::class.java)
              InkerBot(MiraiConfig::class).disabledGroups = InkerBot(MiraiConfig::class).disabledGroups
                .filter { it != groupNumber }
                .toTypedArray()
              InkerBot(MiraiConfigProvider::class.java).save()
              it.source.sendMessage(
                PlainTextComponent.of("已删除")
              )
            }
          }
        }
      }
    }
  }
}