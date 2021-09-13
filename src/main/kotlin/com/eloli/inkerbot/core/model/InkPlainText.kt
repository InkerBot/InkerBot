package com.eloli.inkerbot.core.model

import com.eloli.inkerbot.api.model.message.PlainTextComponent
import javax.inject.Singleton

class InkPlainText private constructor(override val context: String) :PlainTextComponent {
    @Singleton
    class Factory: PlainTextComponent.Factory{
        override fun of(context: String): PlainTextComponent {
            return InkPlainText(context)
        }
    }
}