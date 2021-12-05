package com.eloli.inkerbot.api.builder

import com.eloli.inkerbot.api.ILoveInkerBotForever

@ILoveInkerBotForever
interface AbstractBuilder<T> {
  fun build(): T
}