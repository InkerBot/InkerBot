package bot.inker.api.event.lifestyle

import bot.inker.api.event.Event
import bot.inker.api.event.message.MessageEvent
import com.eloli.inkcmd.ktdsl.LiteralArgumentBuilderDsl
import com.eloli.inkcmd.tree.CommandNode
import com.google.inject.Binder

interface LifecycleEvent : Event {
  /**
   * During this state, each {@link JvmPlugin} instance
   * has been created.
   */
  interface Construction : BootEvent

  /**
   * Plugins are able to access a default logger instance and access
   * configuration files.
   */
  interface PreInitialization : BootEvent

  /**
   * Plugins should finish any work needed to become functional. Commands
   * should be registered at this stage.
   */
  interface Initialization : BootEvent

  /**
   * Plugins have been initialized and should be ready for action. Loggers,
   * configurations, and third party plugin APIs should be prepared for
   * interaction.
   */
  interface PostInitialization : BootEvent

  /**
   * All plugin initialization and registration should be completed. The
   * bot is now ready to start.
   */
  interface LoadComplete : BootEvent

  /**
   * The {@link Server} instance exists, but worlds have not yet loaded.
   */
  interface ServerAboutToStart : BootEvent

  /**
   * The server instance exists and connections in providers have been created.
   */
  interface ServerStarting : BootEvent

  /**
   * The server is fully loaded and ready to accept clients. All providers are
   * connected and all plugins have been loaded.
   */
  interface ServerStarted : BootEvent

  /**
   * Server is stopping for any reason. This occurs prior to data saving.
   */
  interface ServerStopping : BootEvent

  /**
   * The server has stopped saving and no players are connected. Any changes
   * to the worlds are not saved.
   */
  interface ServerStopped : BootEvent

  /**
   * The bot is stopping, all network connections should be closed, all
   * plugins should prepare for shutdown, closing all files.
   *
   * <p>Note: In the case that the JVM is terminated, this state may never
   * be reached.</p>
   */
  interface BotStopping : BootEvent

  /**
   * The bot has stopped and the JVM will exit. Plugins shouldn't expect to
   * receive this event as all files and connections should be terminated.
   *
   * <p>Note: In the case that the JVM is terminated, this state may never
   * be reached.</p>
   */
  interface BotStopped : BootEvent

  interface BootEvent : LifecycleEvent {
    val lifeCycle:LifeCycle;
  }

  interface RegisterService : LifecycleEvent {
    fun register(binder: Binder.()->Unit)
  }

  interface RegisterEntity : LifecycleEvent {
    fun register(entity: Class<*>)
  }

  interface RegisterCommand : LifecycleEvent {
    fun register(command: CommandNode<MessageEvent>)
    fun register(name:String, action:LiteralArgumentBuilderDsl<MessageEvent>.()->Unit)
  }
}