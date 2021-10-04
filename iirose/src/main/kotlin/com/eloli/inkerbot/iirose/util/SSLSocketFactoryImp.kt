package com.eloli.inkerbot.iirose.util

import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException
import java.security.KeyStore
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class SSLSocketFactoryImp(keyStore: KeyStore?) : SSLSocketFactory() {
    val sSLContext = SSLContext.getInstance("SSL")
    private var trustManager: TrustManager? = null
    fun getTrustManager(): X509TrustManager {
        return trustManager as X509TrustManager
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return emptyArray()
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return emptyArray()
    }

    @Throws(IOException::class)
    override fun createSocket(): Socket {
        return sSLContext.socketFactory.createSocket()
    }

    @Throws(IOException::class)
    override fun createSocket(socket: Socket, host: String, post: Int, autoClose: Boolean): Socket {
        return sSLContext.socketFactory.createSocket(socket, host, post, autoClose)
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(s: String, i: Int): Socket? {
        return null
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(s: String, i: Int, inetAddress: InetAddress, i1: Int): Socket? {
        return null
    }

    @Throws(IOException::class)
    override fun createSocket(inetAddress: InetAddress, i: Int): Socket? {
        return null
    }

    @Throws(IOException::class)
    override fun createSocket(inetAddress: InetAddress, i: Int, inetAddress1: InetAddress, i1: Int): Socket? {
        return null
    }

    init {
        trustManager = object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(x509Certificates: Array<X509Certificate>, s: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(x509Certificates: Array<X509Certificate>, s: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                //注意这里不能返回null，否则会报错,如下面错误[1]
                return emptyArray()
            }
        }
        sSLContext.init(null, arrayOf<TrustManager>(trustManager as X509TrustManager), null)
    }
}