package com.eloli.inkerbot.core.command

interface CommandNode {
    val name:String
    val usage:String
    val describe:String
    val children:Collection<CommandNode>
    fun addChild(child: CommandNode)
}