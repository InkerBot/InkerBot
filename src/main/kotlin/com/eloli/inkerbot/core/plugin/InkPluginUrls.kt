package com.eloli.inkerbot.core.plugin

import com.eloli.inkerbot.api.plugin.PluginUrls
import java.util.*

class InkPluginUrls(
    override var home: Optional<String>,
    override var source: Optional<String>,
    override var issue: Optional<String>
) :PluginUrls {
    class Builder:PluginUrls.Builder{
        var home: Optional<String> = Optional.empty()
        var source: Optional<String> = Optional.empty()
        var issue: Optional<String> = Optional.empty()
        override fun home(home: String?): PluginUrls.Builder {
            this.home = Optional.ofNullable(home)
            return this
        }

        override fun source(source: String?): PluginUrls.Builder {
            this.source = Optional.ofNullable(source)
            return this
        }

        override fun issue(issue: String?): PluginUrls.Builder {
            this.issue = Optional.ofNullable(issue)
            return this
        }

        override fun build(): PluginUrls {
            return InkPluginUrls(home, source, issue)
        }
    }
}