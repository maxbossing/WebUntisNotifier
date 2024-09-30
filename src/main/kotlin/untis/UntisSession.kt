package untis

import config.Untis
import io.ktor.utils.io.errors.*
import org.bytedream.untis4j.LoginException
import org.bytedream.untis4j.Session
import utils.w
import java.time.LocalDate

inline fun closingUntisSession(config: Untis, block: (session: Session) -> Unit) =
    try {
        Session.login(config.username, config.password, config.server, config.school).apply(block).logout()
    } catch(e: LoginException) {
        w("failed to login to untis")
        e.printStackTrace()
    } catch(e: IOException) {
        w("unknown exception occurred")
        e.printStackTrace()
    }


fun Session.todaysTimetable() = getTimetableFromPersonId(LocalDate.now(), LocalDate.now(), infos.personId)