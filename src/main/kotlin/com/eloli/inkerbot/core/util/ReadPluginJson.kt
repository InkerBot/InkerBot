package com.eloli.inkerbot.core.util

import com.eloli.inkerbot.api.plugin.PluginDepend
import com.eloli.inkerbot.api.plugin.PluginMeta
import com.eloli.inkerbot.api.plugin.PluginUrls
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.Reader
import java.util.*

class ReadPluginJson {
    companion object{
        private val gson:Gson = Gson();
        fun read(reader: Reader):PluginMeta {
            return gson.fromJson(reader, JsonPluginMeta::class.java)
        }
    }

    class JsonPluginMeta:PluginMeta {
        override lateinit var name: String
        override lateinit var describe: String
        override lateinit var version: String
        override lateinit var urls: JsonPluginUrls
        override lateinit var main: String
        override lateinit var depends: Collection<JsonPluginDepend>
    }

    class JsonPluginUrls:PluginUrls {
        override val home: Optional<String>
            get() {
                return Optional.ofNullable(real_home)
            }
        override val source: Optional<String>
            get() {
                return Optional.ofNullable(real_source)
            }
        override val issue: Optional<String>
            get() {
                return Optional.ofNullable(real_issue)
            }

        @SerializedName("home")
        var real_home: String? = null
        @SerializedName("source")
        var real_source: String? = null
        @SerializedName("issue")
        var real_issue: String? = null
    }

    class JsonPluginDepend:PluginDepend {
        override lateinit var name: String
        override lateinit var type: PluginDepend.Type
    }
}