package com.eloli.inkerbot.api.event

import kotlin.Throws
import java.lang.Exception

interface EventListener<T> {
    @Throws(Exception::class)
    fun handle(event: T)
}