package org.gotson.nestor.domain.service

import mu.KotlinLogging
import org.gotson.nestor.domain.model.BookingResult
import org.gotson.nestor.domain.model.ClassRequest
import org.gotson.nestor.domain.model.CredentialsException
import org.gotson.nestor.domain.model.ScheduleConflictException
import org.gotson.nestor.infrastructure.encryption.EncryptionService
import org.gotson.nestor.infrastructure.selenium.PureDriverBuilder
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class BookingService(
        private val encryptionService: EncryptionService,
        private val busyTimeFilterer: BusyTimeFilterer,
        private val pureDriverBuilder: PureDriverBuilder,
        private val notificationSender: NotificationSender
) {

    @Async
    fun bookPureYoga(classRequest: ClassRequest) {
        logger.info { "Wished class: $classRequest" }

        try {
            busyTimeFilterer.checkForConflicts(classRequest)
        } catch (e: ScheduleConflictException) {
            logger.info(e) { "Schedule conflict: ${e.busyTime.summary}" }
            notificationSender.notifyScheduleConflict(e.busyTime, classRequest)
            return
        }

        val decryptedUserName = encryptionService.decrypt(classRequest.membership.login)
        val decryptedPassword = encryptionService.decrypt(classRequest.membership.password)

        try {
            pureDriverBuilder.build().use { pureDriver ->
                pureDriver.of(classRequest.membership.studio.url)
                        .performUserLogin(decryptedUserName, decryptedPassword)
                        .setAllLocation()
                        .setDate(classRequest.dateTime)
                        .parse()

                //find matching candidates
                val matchingClasses = pureDriver.bookableClasses.filter { classRequest.matches(it) }

                logger.info { "Found ${matchingClasses.size} matching classes:" }
                matchingClasses.forEach { logger.info { it } }

                if (matchingClasses.size == 1) {
                    val classToBook = matchingClasses.first()
                    logger.info { "Found 1 matching class, trying to book it: $classToBook" }
                    val result = pureDriver.book(classToBook)
                    when (result) {
                        BookingResult.BOOKED -> notificationSender.notifySuccessfulBooking(classToBook, classRequest.membership.user)
                        BookingResult.WAITLIST -> notificationSender.notifyWaitlistedBooking(classToBook, classRequest.membership.user)
                        BookingResult.ERROR -> notificationSender.notifyBookingError(classToBook, classRequest.membership.user)
                    }
                    logger.info { "Found a matching class: $classToBook, booking result: $result" }
                } else {
                    notificationSender.notifyNoMatchFound(classRequest)
                    logger.info { "There is not exactly 1 candidate for booking, nothing done" }
                }
            }
        } catch (e: CredentialsException) {
            logger.error(e) { e.message }
            notificationSender.notifyCredentialsError(classRequest)
        } catch (e: Exception) {
            logger.error(e) { "An error occurred while booking: ${e.message}" }
            notificationSender.notifyGenericError(classRequest)
        }
    }
}