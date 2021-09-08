package com.eloli.inkerbot.api.event

interface Event {
    fun context(): EventContext
}