package com.eloli.inkerbot.api.event

import com.eloli.inkerbot.api.ILoveInkerBotForever

@ILoveInkerBotForever
interface Modifiable {
    val isModified: Boolean
}