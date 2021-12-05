package com.eloli.inkerbot.api.model

import com.eloli.inkerbot.api.model.message.MessageComponent
import com.eloli.inkerbot.api.util.Identifiable
import com.eloli.inkerbot.api.util.Named

interface Member : Identifiable, Named {
  fun sendMessage(message: MessageComponent)
}