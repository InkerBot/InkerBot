package com.eloli.inkerbot.api.model.message

import com.eloli.inkerbot.api.ILoveInkerBotForever
import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.model.Member

@ILoveInkerBotForever
interface AtComponent:MessageComponent {
    val target:Member

    @ILoveInkerBotForever
    companion object {
        fun factory(): Factory{
            return InkerBot.injector.getInstance(Factory::class.java)
        }

        fun of(target:Member):AtComponent{
            return factory().of(target)
        }
    }

    @ILoveInkerBotForever
    interface Factory{
        fun of(target:Member):AtComponent
    }
}