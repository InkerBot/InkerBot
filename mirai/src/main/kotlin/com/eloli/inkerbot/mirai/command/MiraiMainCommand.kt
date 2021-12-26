package com.eloli.inkerbot.mirai.command

import bot.inker.api.event.AutoComponent
import bot.inker.api.event.EventHandler
import bot.inker.api.event.lifestyle.LifecycleEvent
import com.eloli.inkcmd.builder.LiteralArgumentBuilder
import javax.inject.Singleton

@Singleton
@AutoComponent
class MiraiMainCommand {
  @EventHandler
  fun onRegisterCommand(event:LifecycleEvent.RegisterCommand){
    event.register("mirai"){
      describe = "管理 Mirai 的Bot"
      ConfigCommand(this)
    }
  }
}