package bot.inker.iirose

import bot.inker.api.event.AutoComponent
import bot.inker.api.event.EventHandler
import bot.inker.api.event.EventManager
import bot.inker.api.event.Order
import bot.inker.api.event.lifestyle.LifecycleEvent
import bot.inker.api.plugin.PluginContainer
import bot.inker.iirose.config.IbConfig
import bot.inker.iirose.event.IbRawMessageEvent
import bot.inker.iirose.event.IbSendRawMessageEvent
import bot.inker.iirose.event.IbSocketEvent
import bot.inker.iirose.packet.LoginJson
import bot.inker.iirose.util.CodeUtil
import bot.inker.iirose.util.SSLSocketFactoryImp
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okio.ByteString
import okio.ByteString.Companion.decodeHex
import org.slf4j.Logger
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.timer

@Singleton
@AutoComponent
class IbConnection {
  @Inject
  private lateinit var ibConfig: IbConfig

  @Inject
  private lateinit var wsListener: WsListener

  @Inject
  private lateinit var logger: Logger

  @Inject
  private lateinit var eventManager: EventManager

  @Inject
  private lateinit var plugin: PluginContainer
  private val gson = Gson()

  private lateinit var client: OkHttpClient
  private lateinit var request: Request
  private lateinit var ws: WebSocket
  private lateinit var timer: Timer

  @EventHandler
  fun onBoot(event: LifecycleEvent.ServerStarted) {
    runBlocking {
      val trustStore = KeyStore.getInstance(
        KeyStore
          .getDefaultType()
      );
      trustStore.load(null, null);
      val ssl = SSLSocketFactoryImp(trustStore)
      client = OkHttpClient.Builder()
        .pingInterval(5, TimeUnit.SECONDS)
        .sslSocketFactory(ssl.sSLContext.socketFactory, ssl.getTrustManager())
        .hostnameVerifier { _, _ -> true }
        .build()
      request = Request.Builder()
        .url(ibConfig.wsUrl)
        .build()

      connect()

      timer = timer(period = 5000) {
        ws.send("s")
      }
    }
  }

  fun close() {
    ws.close(1000, "InkerBot need to switch account.")
  }

  @EventHandler
  fun onMessage(event: IbSocketEvent.Message) {
    for (s in event.message.split("<")) {
      eventManager.post(IbRawMessageEvent(s))
    }
  }

  @EventHandler(order = Order.POST)
  fun onSendMessage(event: IbSendRawMessageEvent) {
    ws.send(event.message)
  }

  fun connect() {
    ws = client.newWebSocket(request, wsListener)
    ws.send(
      LoginJson.Factory.of(
        ibConfig.room,
        ibConfig.username,
        ibConfig.password
      ).toString()
    )
  }

  @EventHandler(order = Order.POST)
  fun onFailure(event: IbSocketEvent.Failure) {
    runBlocking {
      connect()
    }
  }

  class WsListener : WebSocketListener() {
    @Inject
    private lateinit var logger: Logger

    @Inject
    private lateinit var eventManager: EventManager
    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
      logger.info("onClose: code={}, reason={}", code, reason)
      eventManager.post(IbSocketEvent.Closed(code, reason))
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
      logger.info("onClosing: code={}, reason={}", code, reason)
      eventManager.post(IbSocketEvent.Closing(code, reason))
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
      logger.info("onOpen: ")
      eventManager.post(IbSocketEvent.Open())
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
      logger.info("onFailure:", t)
      eventManager.post(IbSocketEvent.Failure(t))
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
      logger.debug("onMessageBytes: size={}", bytes.size)
      val message: String = if (bytes.startsWith("01".decodeHex())) {
        CodeUtil.deGzip(bytes.substring(1).toByteArray()).toString(Charsets.UTF_8)
      } else {
        bytes.string(Charsets.UTF_8)
      }
      logger.trace("onMessageBytes: message={}", message)
      eventManager.post(IbSocketEvent.Message(message))
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
      logger.debug("onMessageString: {}", text)
      eventManager.post(IbSocketEvent.Message(text))
    }
  }
}