package bot.inker.api.event.lifestyle

enum class LifeCycle {
  /**
   * During this state, each {@link JvmPlugin} instance
   * has been created.
   */
  CONSTRUCTION,

  /**
   * Plugins are able to access a default logger instance and access
   * configuration files.
   */
  PRE_INITIALIZATION,

  /**
   * Plugins should finish any work needed to become functional. Commands
   * services and entities should be registered in this stage.
   */
  INITIALIZATION,

  /**
   * Plugins have been initialized and should be ready for action. Loggers,
   * configurations, and third party plugin APIs should be prepared for
   * interaction.
   */
  POST_INITIALIZATION,

  /**
   * All plugin initialization and registration should be completed. The
   * bot is now ready to start.
   */
  LOAD_COMPLETE,

  /**
   * The {@link Server} instance exists, but worlds have not yet loaded.
   */
  SERVER_ABOUT_TO_START,

  /**
   * The server instance exists and connections in providers have been created.
   */
  SERVER_STARTING,

  /**
   * The server is fully loaded and ready to accept clients. All providers are
   * connected and all plugins have been loaded.
   */
  SERVER_STARTED,

  /**
   * Server is stopping for any reason. This occurs prior to data saving.
   */
  SERVER_STOPPING,

  /**
   * The server has stopped saving and no players are connected. Any changes
   * to the worlds are not saved.
   */
  SERVER_STOPPED,

  /**
   * The bot is stopping, all network connections should be closed, all
   * plugins should prepare for shutdown, closing all files.
   *
   * <p>Note: In the case that the JVM is terminated, this state may never
   * be reached.</p>
   */
  BOT_STOPPING,

  /**
   * The bot has stopped and the JVM will exit. Plugins shouldn't expect to
   * receive this event as all files and connections should be terminated.
   *
   * <p>Note: In the case that the JVM is terminated, this state may never
   * be reached.</p>
   */
  BOT_STOPPED;
}