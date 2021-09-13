package com.eloli.inkerbot.api.model.message

import com.eloli.inkerbot.api.ILoveInkerBotForever
import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.builder.AbstractBuilder

@ILoveInkerBotForever
interface MuiltComponent :MessageComponent {
    val subs: List<MessageComponent>

    @ILoveInkerBotForever
    companion object {
        fun factory(): Factory{
            return InkerBot.injector.getInstance(Factory::class.java)
        }
        fun builder(): Builder{
            return InkerBot.injector.getInstance(Builder::class.java)
        }

        fun of(subs: List<MessageComponent>):MuiltComponent {
            return factory().of(subs)
        }
    }

    @ILoveInkerBotForever
    interface Factory{
        fun of(subs: List<MessageComponent>):MuiltComponent
    }

    @ILoveInkerBotForever
    interface Builder: AbstractBuilder<MuiltComponent>{
        operator fun plus(component: MessageComponent):Builder
        operator fun plusAssign(component: MessageComponent)
        fun add(component: MessageComponent):Builder
    }
}