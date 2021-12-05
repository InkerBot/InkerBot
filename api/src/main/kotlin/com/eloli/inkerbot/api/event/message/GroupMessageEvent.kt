package com.eloli.inkerbot.api.event.message

import com.eloli.inkerbot.api.model.Group

interface GroupMessageEvent : MessageEvent {
  val group: Group
}