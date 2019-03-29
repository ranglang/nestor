package org.gotson.nestor.domain.service

import org.gotson.nestor.domain.model.BusyTime
import org.gotson.nestor.domain.model.ClassRequest
import org.gotson.nestor.domain.model.PlannedClass
import org.gotson.nestor.domain.model.User
import org.gotson.nestor.infrastructure.email.EmailSender
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class NotificationSender constructor(
        @Value("\${nestor.email.format.date:EEE d MMM yyyy}")
        private val dateFormat: String,

        @Value("\${nestor.email.format.time:HH:mm}")
        private val timeFormat: String,

        private val emailSender: EmailSender
) {
    private val formatterDate = DateTimeFormatter.ofPattern(dateFormat)
    private val formatterTime = DateTimeFormatter.ofPattern(timeFormat)

    fun notifySuccessfulBooking(plannedClass: PlannedClass, user: User) {
        val subject = "[Nestor] Booked ${plannedClass.type} at ${plannedClass.dateTime.format(formatterTime)} on ${plannedClass.dateTime.format(formatterDate)}"
        val body = """Nestor booked a class for you!
                    |
                    |Class: ${plannedClass.type}
                    |Instructor: ${plannedClass.instructor}
                    |Date and time: ${plannedClass.dateTime.format(formatterDate)} ${plannedClass.dateTime.format(formatterTime)}
                    |Location: ${plannedClass.location}
                    |
                """.trimMargin()

        emailSender.sendEmail(subject, body, user.email)
    }

    fun notifyWaitlistedBooking(plannedClass: PlannedClass, user: User) {
        val subject = "[Nestor] Waitlisted ${plannedClass.type} at ${plannedClass.dateTime.format(formatterTime)} on ${plannedClass.dateTime.format(formatterDate)}"
        val body = """Nestor booked a class for you on waitlist!
                    |
                    |Class: ${plannedClass.type}
                    |Instructor: ${plannedClass.instructor}
                    |Date and time: ${plannedClass.dateTime.format(formatterDate)} ${plannedClass.dateTime.format(formatterTime)}
                    |Location: ${plannedClass.location}
                    |
                """.trimMargin()

        emailSender.sendEmail(subject, body, user.email)
    }

    fun notifyBookingError(plannedClass: PlannedClass, user: User) {
        val subject = "[Nestor] An error occured while booking"
        val body = """Nestor was trying to book a class for you, but an error occured :(
                    |
                    |Class: ${plannedClass.type}
                    |Instructor: ${plannedClass.instructor}
                    |Date and time: ${plannedClass.dateTime.format(formatterDate)} ${plannedClass.dateTime.format(formatterTime)}
                    |Location: ${plannedClass.location}
                    |
                """.trimMargin()

        emailSender.sendEmail(subject, body, user.email)
    }

    fun notifyCredentialsError(classRequest: ClassRequest) {
        val subject = "[Nestor] could not login to your account"
        val body = """Nestor was trying to book a class for you, but could not login to your account.
                    |Please check that your login and password are correct.
                    |
                    |Wished class
                    |Class: ${classRequest.type}
                    |Date and time: ${classRequest.dateTime.format(formatterDate)} ${classRequest.dateTime.format(formatterTime)}
                    |Location: ${classRequest.location}
                    |
                """.trimMargin()

        emailSender.sendEmail(subject, body, classRequest.membership.user.email)
    }

    fun notifyGenericError(classRequest: ClassRequest) {
        val subject = "[Nestor] An error occured while booking"
        val body = """Nestor was trying to book a class for you, but an error occured :(
                    |
                    |Wished class
                    |Class: ${classRequest.type}
                    |Date and time: ${classRequest.dateTime.format(formatterDate)} ${classRequest.dateTime.format(formatterTime)}
                    |Location: ${classRequest.location}
                    |
                """.trimMargin()

        emailSender.sendEmail(subject, body, classRequest.membership.user.email)
    }

    fun notifyNoMatchFound(classRequest: ClassRequest) {
        val subject = "[Nestor] No matching class found"
        val body = """Nestor was trying to book a class for you, but no match could be found
                    |
                    |Wished class
                    |Class: ${classRequest.type}
                    |Date and time: ${classRequest.dateTime.format(formatterDate)} ${classRequest.dateTime.format(formatterTime)}
                    |Location: ${classRequest.location}
                    |
                """.trimMargin()

        emailSender.sendEmail(subject, body, classRequest.membership.user.email)
    }

    fun notifyScheduleConflict(busyTime: BusyTime, classRequest: ClassRequest) {
        val subject = "[Nestor] Calendar conflict: ${busyTime.summary}"
        val body = """Nestor was trying to book a class for you, but you already have something planned!
                    |
                    |Class: ${classRequest.type}
                    |Date and time: ${classRequest.dateTime.format(formatterDate)} ${classRequest.dateTime.format(formatterTime)}
                    |Location: ${classRequest.location}
                    |
                    |Planned event: ${busyTime.summary}
                    |From: ${busyTime.startDate.format(formatterDate)}
                    |To: ${busyTime.endDate.format(formatterDate)}
                    |
                """.trimMargin()

        emailSender.sendEmail(subject, body, classRequest.membership.user.email)
    }
}