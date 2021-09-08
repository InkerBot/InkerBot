package com.eloli.inkerbot.api.builder

interface AbstractBuilder<T> {
    fun build(): T
}