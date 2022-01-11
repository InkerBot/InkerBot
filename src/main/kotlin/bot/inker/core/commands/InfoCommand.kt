package bot.inker.core.commands

import bot.inker.api.InkerBot
import bot.inker.api.command.permission
import bot.inker.api.event.AutoComponent
import bot.inker.api.event.EventHandler
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.event.message.GroupMessageEvent
import bot.inker.api.model.message.PlainTextComponent
import bot.inker.api.plugin.PluginManager
import bot.inker.api.service.PermissionService
import com.eloli.inkcmd.values.BoolValueType
import org.apache.maven.model.PluginManagement
import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean
import javax.inject.Singleton

@Singleton
@AutoComponent
class InfoCommand {
    @EventHandler
    fun onRegisterCommand(e: LifecycleEvent.RegisterCommand) {
        e.register("info"){
            describe = "Get base info of context."
            permission("inkerbot.command.info")
            executes {
                val expert = it.getOption("expert",Boolean::class.java)
                val source = it.source
                val builder = StringBuilder()
                builder.appendLine("+++ Base information +++")
                builder.appendLine("Running InkerBot v${InkerBot.frame.self.meta.version}")
                builder.appendLine("sender name: ${source.sender.name}")
                builder.appendLine("sender uuid: ${source.sender.identity}")
                if(source is GroupMessageEvent){
                    builder.appendLine("group name: ${source.group.name}")
                    builder.appendLine("group uuid: ${source.group.identity}")
                }
                val hasExpertPermission = InkerBot.serviceManager
                    .getInstance(PermissionService::class.java)
                    .hasPermission(it.source.sender,"inkerbot.command.info.expert")
                if (expert){
                    if(hasExpertPermission) {
                        builder.appendLine("+++ expert output +++")
                        builder.appendLine("Processors: ${Runtime.getRuntime().availableProcessors()}")
                        builder.appendLine("Architecture: ${System.getProperty("os.arch")}-${System.getProperty("sun.arch.data.model")}")
                        builder.appendLine("OS name: ${System.getProperty("os.name")}")
                        builder.appendLine("OS version: ${System.getProperty("os.version")}")
                        builder.appendLine("Java name: ${System.getProperty("java.runtime.name")}")
                        builder.appendLine("Java version: ${System.getProperty("java.runtime.version")}")
                        builder.appendLine("Java vendor: ${System.getProperty("java.vendor")}")
                        builder.appendLine("Jvm name: ${System.getProperty("java.vm.name")}")
                        builder.appendLine("Jvm version: ${System.getProperty("java.vm.version")}")
                        builder.appendLine("Jvm info: ${System.getProperty("java.vm.info")}")
                        builder.appendLine("Launch arguments: ${ManagementFactory.getRuntimeMXBean().inputArguments}")
                        for (plugin in InkerBot(PluginManager::class).plugins) {
                            try {
                                val subBuilder = StringBuilder()
                                subBuilder.appendLine("$plugin: ")
                                subBuilder.appendLine("> version: ${plugin.meta.version}")
                                subBuilder.appendLine("> describe: ${plugin.meta.describe}")
                                plugin.meta.urls.home.ifPresent {
                                    subBuilder.appendLine("> home page: $it")
                                }
                                plugin.meta.urls.source.ifPresent {
                                    subBuilder.appendLine("> source page: $it")
                                }
                                plugin.meta.urls.issue.ifPresent {
                                    subBuilder.appendLine("> issue page: $it")
                                }
                                builder.append(subBuilder)
                            } catch (e: Throwable) {
                                builder.append("Failed to get plugin ${e.message}")
                            }
                        }
                    }else{
                        builder.appendLine("You don't have permissions to get expert output.")
                    }
                }else if(hasExpertPermission){
                    builder.appendLine()
                    builder.appendLine("Add \"--expert\" to get more output.")
                }
                it.source.sendMessage(PlainTextComponent.of(builder.toString()))
            }
            option("expert",BoolValueType.bool()){
                describe("Enable expert output.")
                defaultValue(false)
                defineValue(true)
            }
        }
    }
}