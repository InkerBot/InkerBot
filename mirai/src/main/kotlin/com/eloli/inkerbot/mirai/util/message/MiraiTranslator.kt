package com.eloli.inkerbot.mirai.util.message

import bot.inker.api.model.message.AtComponent
import bot.inker.api.model.message.MessageComponent
import bot.inker.api.model.message.MuiltComponent
import bot.inker.api.model.message.PlainTextComponent
import com.eloli.inkerbot.mirai.model.MiraiMember
import net.mamoe.mirai.message.data.*

object MiraiTranslator{
  fun toMirai(message: MessageComponent): Message {
    return toMirai(message,MessageChainBuilder()).build()
  }

  fun toMirai(message: MessageComponent,builder:MessageChainBuilder):MessageChainBuilder {
    when (message) {
      is MuiltComponent -> {
        toMirai(message,builder)
      }
      is PlainTextComponent -> {
        builder.add(PlainText(message.toString()))
      }
      is AtComponent -> {
        val target = message.target
        if(target is MiraiMember){
          builder.add(At(target.qqNumber.toLong()))
        }else{
          builder.add(PlainText(" [inkat,\"${target.key}\",\"${target.identity}\"] "))
        }
      }
    }
    return builder
  }


  fun toInk(message: Message): MessageComponent {
    return toInk(message,MuiltComponent.builder()).build()
  }

  fun toInk(message: Message, builder: MuiltComponent.Builder):MuiltComponent.Builder{
    when (message) {
      is MessageChain -> {
        for (sub in message) {
          toInk(sub,builder)
        }
      }
      is At -> {
        builder.add(AtComponent.of(MiraiMember.update(message.target.toString()){
          try {
            it.name
          }catch (e:UninitializedPropertyAccessException){
            it.name = "[?]"
          }
        }))
      }
      is MessageContent -> {
        builder.add(PlainTextComponent.of(message.content))
      }
    }
    return builder
  }
}