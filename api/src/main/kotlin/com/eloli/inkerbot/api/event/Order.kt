package com.eloli.inkerbot.api.event

enum class Order(private val value: Int) {
    PRE(-4),
    AFTER_PRE(-3),
    FIRST(-2),
    EARLY(-1),
    DEFAULT(0),
    LATE(1),
    LAST(2),
    BEFORE_POST(3),
    POST(4);

    fun order(): Int {
        return value
    }
}