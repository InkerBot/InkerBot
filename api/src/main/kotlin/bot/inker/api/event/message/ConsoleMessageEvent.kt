package bot.inker.api.event.message

import bot.inker.api.InkerBot

interface ConsoleMessageEvent : MessageEvent {
    val content:String
    interface Factory{
        fun of(content:String):ConsoleMessageEvent
    }
    companion object{
        fun of(content:String):ConsoleMessageEvent{
            return InkerBot(Factory::class).of(content)
        }
    }
}