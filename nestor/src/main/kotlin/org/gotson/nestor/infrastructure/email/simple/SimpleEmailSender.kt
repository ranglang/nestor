package org.gotson.nestor.infrastructure.email.simple

import mu.KotlinLogging
import org.gotson.nestor.infrastructure.email.EmailSender
import org.springframework.context.annotation.Profile
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Profile("email")
@Service
class SimpleEmailSender(
    private val emailSender: JavaMailSender

) : EmailSender {
  override fun sendEmail(subject: String, body: String, to: String) {
    val msg = SimpleMailMessage()
    msg.setTo(to)
    msg.setSubject(subject)
    msg.setText(body)

    emailSender.send(msg)
    logger.info { "Sent mail to $to: $subject" }
  }
}