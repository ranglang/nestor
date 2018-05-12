package org.gotson.nestor.domain.service

import org.gotson.nestor.domain.model.FilterResult
import org.gotson.nestor.domain.model.WishedClassDated
import org.gotson.nestor.infrastructure.email.EmailSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@ConditionalOnBean(EmailSender::class)
@Service
class NotificationSender @Autowired constructor(
    @Value("\${nestor.email.format.date:EEE d MMM yyyy}")
    private val dateFormat: String,

    @Value("\${nestor.email.format.time:HH:mm}")
    private val timeFormat: String,

    private val emailSender: EmailSender
) {
  private val formatterDate = DateTimeFormatter.ofPattern(dateFormat)
  private val formatterTime = DateTimeFormatter.ofPattern(timeFormat)

  fun notifyBookingError(filterResult: FilterResult, bookingRequest: WishedClassDated) {
    val subject = "[Nestor] Calendar conflict: ${filterResult.busyTime?.summary}"
    val body = """Nestor was trying to book a class for you, but you already have something planned!
                    |
                    |Class: ${bookingRequest.type}
                    |Date and time: ${bookingRequest.dateTime.format(formatterDate)} ${bookingRequest.dateTime.format(formatterTime)}
                    |Location: ${bookingRequest.location}
                    |
                    |Planned event: ${filterResult.busyTime?.summary}
                    |From: ${filterResult.busyTime?.startDate?.format(formatterDate)}
                    |To: ${filterResult.busyTime?.endDate?.format(formatterDate)}
                    |
                """.trimMargin()

    emailSender.sendEmail(subject, body, bookingRequest.user.email)
  }
}