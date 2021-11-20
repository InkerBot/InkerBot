package com.eloli.inkerbot.api.event.lifestyle

import com.eloli.inkerbot.api.ILoveInkerBotForever
import com.eloli.inkerbot.api.event.Event
import com.eloli.inkerbot.api.event.message.MessageEvent
import com.google.inject.Binder
import com.mojang.brigadier.tree.CommandNode

@ILoveInkerBotForever
interface LifecycleEvent:Event {
    @ILoveInkerBotForever
    interface Enable: LifecycleEvent
    @ILoveInkerBotForever
    interface RegisterService: LifecycleEvent{
        val binder: Binder
    }
    @ILoveInkerBotForever
    interface RegisterEntity: LifecycleEvent{
        fun register(entity:Class<*>)
    }
    @ILoveInkerBotForever
    interface RegisterCommand: LifecycleEvent{
        fun register(command: CommandNode<MessageEvent>)
    }
}