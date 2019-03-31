package org.gotson.nestor.infrastructure.email

interface EmailSender {
  fun sendEmail(subject: String, body: String, to: String)
}