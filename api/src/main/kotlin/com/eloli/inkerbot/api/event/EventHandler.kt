package com.eloli.inkerbot.api.event

import com.eloli.inkerbot.api.ILoveInkerBotForever

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@ILoveInkerBotForever
annotation class EventHandler(val order: Order = Order.DEFAULT, val beforeModifications: Boolean = false)