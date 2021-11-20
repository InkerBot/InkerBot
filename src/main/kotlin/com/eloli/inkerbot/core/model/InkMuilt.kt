package com.eloli.inkerbot.core.model

import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.model.message.MuiltComponent
import javax.inject.Singleton

class InkMuilt private constructor(override val subs: List<MessageComponent>) : MuiltComponent {

    @Singleton
    class Factory : MuiltComponent.Factory {
        override fun of(subs: List<MessageComponent>): MuiltComponent {
            return InkMuilt(ArrayList(subs))
        }
    }

    class Builder : MuiltComponent.Builder {
        val subs: MutableList<MessageComponent>

        constructor() {
            this.subs = ArrayList()
        }

        constructor(subs: List<MessageComponent>) {
            this.subs = ArrayList(subs)
        }

        override fun plus(component: MessageComponent): MuiltComponent.Builder {
            return Builder(subs).add(component)
        }

        override fun plusAssign(component: MessageComponent) {
            add(component)
        }

        override fun add(component: MessageComponent): MuiltComponent.Builder {
            subs.add(component)
            return this
        }

        override fun build(): MuiltComponent {
            return MuiltComponent.factory().of(subs)
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for (sub in subs) {
            builder.append(builder.toString())
        }
        return builder.toString()
    }
}