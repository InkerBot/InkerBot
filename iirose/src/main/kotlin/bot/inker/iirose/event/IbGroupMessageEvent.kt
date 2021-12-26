package bot.inker.iirose.event

import bot.inker.api.event.AutoComponent
import bot.inker.api.event.EventContext
import bot.inker.api.event.EventHandler
import bot.inker.api.event.EventManager
import bot.inker.api.event.message.GroupMessageEvent
import bot.inker.api.model.Group
import bot.inker.api.model.Member
import bot.inker.api.model.message.MessageComponent
import bot.inker.api.model.message.PlainTextComponent
import bot.inker.iirose.config.IbConfig
import bot.inker.iirose.model.IbGroup
import bot.inker.iirose.model.IbMember
import bot.inker.iirose.util.message.IbTranslator
import javax.inject.Inject
import javax.inject.Singleton

class IbGroupMessageEvent(
  message: String,
  val color: String,
  val id: String,
  val userId: String,
  val userName: String,
  val userTag: String,
  val avatar: String,
  val time: Long
) : GroupMessageEvent,IbMessageEvent {
  override var cancelled: Boolean = false
  override val context: EventContext = EventContext.empty()
  override val sender: Member = IbMember.update(userId) {
    it.name = userName
    it.avatar = avatar
    it.userTag = userTag
  }
  override val group: Group = IbGroup.current()

  override val message: MessageComponent = IbTranslator.toInk(message)
  override fun sendMessage(message: MessageComponent) {
    group.sendMessage(message)
  }

  override fun toString(): String {
    return "IbGroupMessageEvent(color='$color', id='$id', userId='$userId', userName='$userName', userTag='$userTag', avatar='$avatar', time=$time, sender=$sender, group=$group, message=$message)"
  }

  @Singleton
  @AutoComponent
  class Resolver {
    private val startAt = System.currentTimeMillis() / 1000

    @Inject
    private lateinit var config: IbConfig

    @Inject
    private lateinit var eventManager: EventManager

    @EventHandler
    fun onMessage(event: IbRawMessageEvent) {
      if (event.split.size == 11
        && !event.split[0].startsWith("\"")
        && event.split[0].toLong() > startAt
        && event.split[2] != config.username
      ) {
        eventManager.post(
          IbGroupMessageEvent(
            event.split[3],
            event.split[4],
            event.split[10],
            event.split[8],
            event.split[2],
            event.split[9],
            event.split[1],
            event.split[0].toLong()
          )
        )
      }
    }
  }
}