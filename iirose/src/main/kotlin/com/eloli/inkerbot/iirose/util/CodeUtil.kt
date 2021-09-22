package com.eloli.inkerbot.iirose.util

import org.apache.commons.text.StringEscapeUtils
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream


object CodeUtil {
    fun deGzip(bytes: ByteArray): ByteArray {
        if (bytes.isEmpty()) {
            return byteArrayOf()
        }
        val out = ByteArrayOutputStream()
        val input = ByteArrayInputStream(bytes)
        try {
            val ungzip = GZIPInputStream(input)
            val buffer = ByteArray(4096)
            var n: Int
            while (ungzip.read(buffer).also { n = it } >= 0) {
                out.write(buffer, 0, n)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return out.toByteArray()
    }

    fun decode(input: String): String {
        return StringEscapeUtils.UNESCAPE_HTML4.translate(input)
    }

    fun decode(input: List<String>): List<String> {
        return input.map {
            decode(it)
        }
    }

    fun decode(input: Array<String>): Array<String> {
        return input.map {
            decode(it)
        }.toTypedArray()
    }

    fun encode(input: String): String {
        return StringEscapeUtils.ESCAPE_HTML4.translate(input)
    }

    fun encode(input: List<String>): List<String> {
        return input.map {
            decode(it)
        }
    }

    fun encode(input: Array<String>): Array<String> {
        return input.map {
            decode(it)
        }.toTypedArray()
    }
}