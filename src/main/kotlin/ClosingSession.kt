import io.ktor.utils.io.errors.*
import org.bytedream.untis4j.LoginException
import org.bytedream.untis4j.Session

inline fun closingUntisSession(config: Untis,  block: (Session) -> Unit) =
    try {
        Session.login(config.username, config.password, config.server, config.school).apply(block).logout()
    } catch(e: LoginException) {
        println("failed to login")
    } catch(e: IOException) {
        e.printStackTrace()
    }

