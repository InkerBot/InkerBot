package com.eloli.inkerbot.api.event

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class EventHandler(val order: Order = Order.DEFAULT, val beforeModifications: Boolean = false)