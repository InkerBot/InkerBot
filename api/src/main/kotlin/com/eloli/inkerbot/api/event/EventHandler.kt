package com.eloli.inkerbot.api.event

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class EventHandler(val order: Order = Order.DEFAULT, val beforeModifications: Boolean = false)