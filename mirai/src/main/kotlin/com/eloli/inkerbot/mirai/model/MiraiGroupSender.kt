package com.eloli.inkerbot.mirai.model

import com.eloli.inkerbot.api.model.Sender
import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.util.Identity
import com.eloli.inkerbot.api.util.ResourceKey
import com.eloli.inkerbot.mirai.util.MiraiMessageUtil
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.nameCardOrNick

class MiraiGroupSender(private val sender: Member):Sender {
    override val name: String = sender.nameCardOrNick
    override fun sendMessage(message: MessageComponent) {
        runBlocking {
            sender.sendMessage(MiraiMessageUtil.toMirai(message))
        }
    }
    override val identity: Identity = Identity.of(sender.id.toString())
    override val key: ResourceKey = ResourceKey.of("mirai", "member")
}