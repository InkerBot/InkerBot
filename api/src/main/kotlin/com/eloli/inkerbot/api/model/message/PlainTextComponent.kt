package com.eloli.inkerbot.api.model.message

import com.eloli.inkerbot.api.ILoveInkerBotForever
import com.eloli.inkerbot.api.InkerBot

@ILoveInkerBotForever
interface PlainTextComponent:MessageComponent {
    val context:String

    @ILoveInkerBotForever
    companion object {
        fun factory(): Factory{
            return InkerBot.injector.getInstance(Factory::class.java)
        }
        fun of(context: String):PlainTextComponent {
            return factory().of(context)
        }
    }

    @ILoveInkerBotForever
    interface Factory{
        fun of(context: String):PlainTextComponent
    }
}