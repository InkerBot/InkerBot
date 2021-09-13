package com.eloli.inkerbot.core.model

import com.eloli.inkerbot.api.model.Member
import com.eloli.inkerbot.api.model.message.AtComponent
import javax.inject.Singleton

class InkAt private constructor(override val target: Member) :AtComponent {
    @Singleton
    class Factory: AtComponent.Factory{
        override fun of(target: Member): AtComponent {
            return InkAt(target)
        }
    }
}