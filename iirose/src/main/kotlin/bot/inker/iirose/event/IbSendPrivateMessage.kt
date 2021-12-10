package bot.inker.iirose.event

import bot.inker.api.event.*
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

class IbSendPrivateMessage(
  val target: String,
  val message: String,
  val color: String = "ffffff"
) : Event {
  override val context: EventContext = EventContext.empty()

  @Singleton
  class Resolver {
    private val gson = Gson()
    private var messageId = 111111111111L

    @Inject
    private lateinit var eventManager: EventManager

    @EventHandler(order = Order.POST)
    fun onSendRoomEvent(event: IbSendPrivateMessage) {
      eventManager.post(
        IbSendRawMessageEvent(
          gson.toJson(Packet().apply {
            g = event.target
            m = event.message
            mc = event.color
            i = messageId++
          })
        )
      )
    }
  }


  class Packet {
    lateinit var g: String
    lateinit var m: String
    lateinit var mc: String
    var i: Long = 0L
  }

  override fun toString(): String {
    return "IbSendPrivateMessage(target='$target', message='$message', color='$color', context=$context)"
  }
}