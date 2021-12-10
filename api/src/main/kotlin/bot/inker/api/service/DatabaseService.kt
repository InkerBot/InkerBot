package bot.inker.api.service

import org.hibernate.Session

interface DatabaseService {
  val session: Session
}