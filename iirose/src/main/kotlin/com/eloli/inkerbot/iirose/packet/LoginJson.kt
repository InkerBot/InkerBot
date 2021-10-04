package com.eloli.inkerbot.iirose.packet

import com.google.gson.Gson
import java.math.BigInteger
import java.security.MessageDigest

class LoginJson {
    lateinit var r: String
    lateinit var n: String
    lateinit var p: String
    lateinit var st: String
    lateinit var mo: String
    lateinit var mb: String
    lateinit var cp: String
    lateinit var mu: String
    lateinit var nt: String
    lateinit var fp: String

    override fun toString(): String {
        return "*" + Gson().toJson(this)
    }

    object Factory {
        fun of(room: String, name: String, password: String): LoginJson {
            return LoginJson().apply {
                r = room
                n = name
                p = md5(password)
                st = "n"
                mo = ""
                mb = ""
                cp = System.currentTimeMillis().toString()
                mu = "01"
                nt = "!6"
                fp = "@" + md5(name)
            }
        }

        private fun md5(input: String): String {
            val md5 = MessageDigest.getInstance("md5")
            return BigInteger(1, md5.digest(input.toByteArray(Charsets.UTF_8)))
                .toString(16)
        }
    }
}