package bot.inker.api.service

import org.hibernate.Session

interface DatabaseService {
  val session: Session
  val entities:Collection<Class<*>>
}