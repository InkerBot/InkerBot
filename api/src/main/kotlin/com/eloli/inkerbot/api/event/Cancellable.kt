package com.eloli.inkerbot.api.event

interface Cancellable : Event {
    var cancelled: Boolean
    fun cancel() {
        cancelled = true
    }
}