package com.eloli.inkerbot.api.event

import com.eloli.inkerbot.api.ILoveInkerBotForever

@ILoveInkerBotForever
interface Cancellable : Event {
  var cancelled: Boolean
  fun cancel() {
    cancelled = true
  }
}