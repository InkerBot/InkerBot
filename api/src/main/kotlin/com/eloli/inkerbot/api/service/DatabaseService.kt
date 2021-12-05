package com.eloli.inkerbot.api.service

import org.hibernate.Session

interface DatabaseService {
  val session: Session
}