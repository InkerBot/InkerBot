package com.eloli.inkerbot.api

import com.google.inject.Injector

interface ServiceManager : Injector {
  val inited: Boolean
}