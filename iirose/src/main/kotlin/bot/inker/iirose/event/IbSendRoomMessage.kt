package bot.inker.iirose.event

import bot.inker.api.event.*
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

class IbSendRoomMessage(
  val message: String,
  val color: String = "ffffff"
) : Event,Cancellable {
  override val context: EventContext = EventContext.empty()
  override var cancelled: Boolean = false

  @Singleton
  @AutoComponent
  class Resolver {
    private val gson = Gson()
    private var messageId = 111111111111L

    @Inject
    private lateinit var eventManager: EventManager

    @EventHandler(order = Order.POST)
    fun onSendRoomEvent(event: IbSendRoomMessage) {
      eventManager.post(
        IbSendRawMessageEvent(
          gson.toJson(Packet().apply {
            m = event.message
            mc = event.color
            i = messageId++
          })
        )
      )
    }
  }

  class Packet {
    lateinit var m: String
    lateinit var mc: String
    var i: Long = 0L
  }

  override fun toString(): String {
    return "IbSendRoomMessage(message='$message', color='$color', context=$context)"
  }
}