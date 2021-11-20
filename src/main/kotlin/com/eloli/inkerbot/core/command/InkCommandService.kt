package com.eloli.inkerbot.core.command

import com.eloli.inkerbot.api.event.EventHandler
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.event.message.MessageEvent
import com.eloli.inkerbot.api.model.message.PlainTextComponent
import com.eloli.inkerbot.api.service.CommandService
import com.eloli.inkerbot.core.event.lifestyle.InkLifecycleEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InkCommandService:CommandService {
    @Inject
    private lateinit var eventManager: EventManager
    override val dispatcher:CommandDispatcher<MessageEvent> get() {

    }

    @EventHandler
    fun onMessageEvent(event: MessageEvent){
        val message = event.message.toString()
        if(message.startsWith("/")){
            try {
                dispatcher.execute(message.substring(1),event)
            }catch (e: CommandSyntaxException){
                event.sendMessage(PlainTextComponent.of(e.message!!))
            }

        }
    }
}