package com.eloli.inkerbot.iirose

import com.eloli.inkerbot.api.event.EventHandler
import com.eloli.inkerbot.api.event.EventManager
import com.eloli.inkerbot.api.event.Order
import com.eloli.inkerbot.api.plugin.PluginContainer
import com.eloli.inkerbot.iirose.config.IbConfig
import com.eloli.inkerbot.iirose.event.IbRawMessageEvent
import com.eloli.inkerbot.iirose.event.IbSendRawMessageEvent
import com.eloli.inkerbot.iirose.event.IbSocketEvent
import com.eloli.inkerbot.iirose.packet.LoginJson
import com.eloli.inkerbot.iirose.util.CodeUtil
import com.eloli.inkerbot.iirose.util.SSLSocketFactoryImp
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okio.ByteString
import okio.ByteString.Companion.decodeHex
import org.slf4j.Logger
import java.security.KeyStore
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.timer

@Singleton
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
    private val gson =Gson()

    private lateinit var ws:WebSocket

    fun onBoot(){
        eventManager.registerListeners(plugin, this)
        runBlocking {
            val trustStore = KeyStore.getInstance(KeyStore
                .getDefaultType());
            trustStore.load(null, null);
            val ssl = SSLSocketFactoryImp(trustStore)
            val client = OkHttpClient.Builder()
                .pingInterval(5, TimeUnit.SECONDS)
                .sslSocketFactory(ssl.sSLContext.socketFactory, ssl.getTrustManager())
                .hostnameVerifier { _, _ -> true }
                .build()
            val request = Request.Builder()
                .url(ibConfig.wsUrl)
                .build()
            ws = client.newWebSocket(request, wsListener)
            ws.send(LoginJson.Factory.of(
                ibConfig.room,
                ibConfig.username,
                ibConfig.password
            ).toString())

            timer(period = 5000) {
                ws.send("s")
            }
        }
    }

    @EventHandler
    fun onMessage(event:IbSocketEvent.Message){
        for (s in event.message.split("<")) {
            eventManager.post(IbRawMessageEvent(s))
        }
    }

    @EventHandler(order = Order.POST)
    fun onSendMessage(event:IbSendRawMessageEvent){
        ws.send(event.message)
    }

    class WsListener : WebSocketListener() {
        @Inject
        private lateinit var logger: Logger
        @Inject
        private lateinit var eventManager: EventManager
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            logger.debug("onClose: code={}, reason={}", code, reason)
            eventManager.post(IbSocketEvent.Closed(code,reason))
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            logger.debug("onClosing: code={}, reason={}", code, reason)
            eventManager.post(IbSocketEvent.Closing(code,reason))
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            logger.debug("onOpen: ")
            eventManager.post(IbSocketEvent.Open())
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            logger.debug("onFailure:", t)
            eventManager.post(IbSocketEvent.Failure(t))
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            logger.debug("onMessageBytes: size={}", bytes.size)
            val message:String = if (bytes.startsWith("01".decodeHex())){
                CodeUtil.deGzip(bytes.substring(1).toByteArray()).toString(Charsets.UTF_8)
            }else{
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