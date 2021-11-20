package com.eloli.inkerbot.iirose

import com.eloli.inkerbot.api.InkerBot
import com.eloli.inkerbot.api.event.EventHandler
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.event.lifestyle.LifecycleEvent
import com.eloli.inkerbot.api.event.message.MessageEvent
import com.eloli.inkerbot.api.model.Group
import com.eloli.inkerbot.api.model.Member
import com.eloli.inkerbot.api.model.message.PlainTextComponent
import com.eloli.inkerbot.api.plugin.JvmPlugin
import com.eloli.inkerbot.api.plugin.PluginContainer
import com.eloli.inkerbot.api.registry.Registrar
import com.eloli.inkerbot.api.registry.UpdatableRegistrar
import com.eloli.inkerbot.api.service.CommandService
import com.eloli.inkerbot.api.service.DatabaseService
import com.eloli.inkerbot.iirose.config.IbConfig
import com.eloli.inkerbot.iirose.config.IbConfigProvider
import com.eloli.inkerbot.iirose.event.IbGroupMessageEvent
import com.eloli.inkerbot.iirose.event.IbPrivateMessageEvent
import com.eloli.inkerbot.iirose.event.IbSendPrivateMessage
import com.eloli.inkerbot.iirose.event.IbSendRoomMessage
import com.eloli.inkerbot.iirose.model.IbGroup
import com.eloli.inkerbot.iirose.model.IbMember
import com.google.inject.Binder
import com.google.inject.TypeLiteral
import com.google.inject.name.Names
import com.mojang.brigadier.arguments.IntegerArgumentType.integer
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.CommandNode
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class IiroseCore:JvmPlugin {
    override fun configure(binder: Binder) {
        binder.bind(IbConfig::class.java).toProvider(IbConfigProvider::class.java)
    }
    @Inject
    private lateinit var databaseService: DatabaseService

    @Inject
    private lateinit var pluginContainer: PluginContainer

    @Inject
    private lateinit var connection: IbConnection

    @Inject
    private lateinit var eventManager: EventManager

    @Inject
    private lateinit var commandService: CommandService

    @EventHandler
    fun onBoot(event: LifecycleEvent.Enable){
        connection.onBoot()
        eventManager.registerListeners(pluginContainer,
            InkerBot.injector.getInstance(IbGroupMessageEvent.Resolver::class.java))
        eventManager.registerListeners(pluginContainer,
            InkerBot.injector.getInstance(IbPrivateMessageEvent.Resolver::class.java))
        eventManager.registerListeners(pluginContainer,
            InkerBot.injector.getInstance(IbSendRoomMessage.Resolver::class.java))
        eventManager.registerListeners(pluginContainer,
            InkerBot.injector.getInstance(IbSendPrivateMessage.Resolver::class.java))
    }

    @EventHandler
    fun onRegisterService(event: LifecycleEvent.RegisterService){
        event.binder.bind(object : TypeLiteral<Registrar<Member>>() {}).annotatedWith(Names.named("iirose:member"))
            .toInstance(UpdatableRegistrar.of(Member::class.java,IbMember::class.java,IbMember.Record::class.java))
        event.binder.bind(object : TypeLiteral<Registrar<Group>>() {}).annotatedWith(Names.named("iirose:room"))
            .toInstance(UpdatableRegistrar.of(Group::class.java,IbGroup::class.java,IbGroup.Record::class.java))
    }

    @EventHandler
    fun onRegisterEntity(event: LifecycleEvent.RegisterEntity){
        event.register(IbGroup.Record::class.java)
        event.register(IbMember.Record::class.java)
    }

    @EventHandler
    fun onRegisterCommand(event: LifecycleEvent.RegisterCommand){

    }

    private fun scanUsage(
        source:MessageEvent,
        command:CommandNode<MessageEvent>,
        superCanUse:Boolean
    ):Collection<String>{
        val result = ArrayList<Pair<String,Collection<String>>>()
        val canUse = command.getRelevantNodes()
        if(canUse){
            result.add(Pair(
                command.usageText,
                if(command is ArgumentCommandNode<*, *>){
                    val examples = ArrayList<String>()
                    examples.add("  Argument: ${command.name}")
                    examples.add("    Examples:")
                    examples.addAll(command.examples.map { "  $it" })
                    examples
                }else{ emptyList() }
            ))
        }
        for (child in command.children) {
            result.addAll(
                scanUsage(source,child,canUse).map {
                    Pair(
                        command.usageText + if(superCanUse){ " [${it.first}]" }else{ " ${it.first}" },
                        it.second
                    )
                }
            )
        }
        return result
    }
    @EventHandler
    fun onEvent(event: MessageEvent){
        // event.sender.sendMessage(event.message)
    }
}