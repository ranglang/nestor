package org.gotson.nestor.domain.service

import org.gotson.nestor.domain.model.PlannedClass
import org.gotson.nestor.domain.model.User
import org.gotson.nestor.domain.model.WishedClassDated
import org.gotson.nestor.infrastructure.email.EmailSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

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

    fun notifySuccessfulBooking(cl: PlannedClass, user: User) {
        val subject = "[Nestor] Booked ${cl.type} at ${cl.dateTime.format(formatterTime)} on ${cl.dateTime.format(formatterDate)}"
        val body = """Nestor booked a class for you!
                    |
                    |Class: ${cl.type}
                    |Instructor: ${cl.instructor}
                    |Date and time: ${cl.dateTime.format(formatterDate)} ${cl.dateTime.format(formatterTime)}
                    |Location: ${cl.location}
                    |
                """.trimMargin()

        emailSender.sendEmail(subject, body, user.email)
    }

    fun notifyWaitlistedBooking(cl: PlannedClass, user: User) {
        val subject = "[Nestor] Waitlisted ${cl.type} at ${cl.dateTime.format(formatterTime)} on ${cl.dateTime.format(formatterDate)}"
        val body = """Nestor booked a class for you on waitlist!
                    |
                    |Class: ${cl.type}
                    |Instructor: ${cl.instructor}
                    |Date and time: ${cl.dateTime.format(formatterDate)} ${cl.dateTime.format(formatterTime)}
                    |Location: ${cl.location}
                    |
                """.trimMargin()

        emailSender.sendEmail(subject, body, user.email)
    }

    fun notifyBookingError(cl: PlannedClass, user: User) {
        val subject = "[Nestor] An error occured while booking"
        val body = """Nestor was trying to book a class for you, but an error occured :(
                    |
                    |Class: ${cl.type}
                    |Instructor: ${cl.instructor}
                    |Date and time: ${cl.dateTime.format(formatterDate)} ${cl.dateTime.format(formatterTime)}
                    |Location: ${cl.location}
                    |
                """.trimMargin()

        emailSender.sendEmail(subject, body, user.email)
    }

    fun notifyNoMatchFound(cl: WishedClassDated) {
        val subject = "[Nestor] No matching class found"
        val body = """Nestor was trying to book a class for you, but no match could be found
                    |
                    |Wished class
                    |Class: ${cl.type}
                    |Date and time: ${cl.dateTime.format(formatterDate)} ${cl.dateTime.format(formatterTime)}
                    |Location: ${cl.location}
                    |
                """.trimMargin()

        emailSender.sendEmail(subject, body, cl.user.email)
    }
}