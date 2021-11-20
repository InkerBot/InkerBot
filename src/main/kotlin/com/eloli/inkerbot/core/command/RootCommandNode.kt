package com.eloli.inkerbot.core.command

import com.eloli.inkerbot.api.event.message.MessageEvent

class RootCommandNode:CommandNode {
    override val name = ""
    override val usage: String = ""
    override val describe: String = "InkerBot Command System"
    override val children = ArrayList<CommandNode>()
    override fun addChild(child: CommandNode) {
        children.add(child)
    }
    fun execute(source:MessageEvent){
        
    }
}