package org.gotson.nestor.infrastructure.email

import mu.KotlinLogging
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Profile("noemail")
@Service
class ConsoleEmailSender : EmailSender {
    override fun sendEmail(subject: String, body: String, to: String) {
        logger.info {
            """Email:
            |Subject: $subject
            |to: $to
            |
            |Body: $body
        """.trimMargin()
        }
    }
}