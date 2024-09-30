package notifications.impl

import config.NtfyNotificationConfig
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import ktor
import notifications.AbstractNotificationProvider
import utils.i

class NtfyNotificationProvider(private val config: NtfyNotificationConfig) : AbstractNotificationProvider<NtfyNotificationConfig>(config) {
    override suspend fun sendMessage(message: String) {
        ktor.post("${config.url}/${config.topic}") {
            contentType(ContentType.Text.Plain)
            setBody(message)
            if (config.username == null) {
                bearerAuth(config.password)
            } else {
                basicAuth(config.username, config.password)
            }
        }.let { response ->
            i("(NtfyRequest): status=${response.status}, message=${response.bodyAsText()}")
        }
    }
}