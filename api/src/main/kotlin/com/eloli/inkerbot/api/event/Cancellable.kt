package com.eloli.inkerbot.api.event

interface Cancellable : Event {
    fun cancelled(): Boolean
    fun cancelled(cancel: Boolean)
    fun cancel() {
        cancelled(true)
    }
}