package org.gotson.nestor.domain.service

import mu.KotlinLogging
import org.gotson.nestor.domain.model.WishedClassDated
import org.gotson.nestor.infrastructure.encryption.EncryptionService
import org.gotson.nestor.infrastructure.selenium.PureDriverBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


private val logger = KotlinLogging.logger {}

@Service
class BookingService @Autowired constructor(
        private val encryptionService: EncryptionService,
        private val pureDriverBuilder: PureDriverBuilder
) {

    fun bookPureYoga(wishedClassDated: WishedClassDated): String {
        logger.info { "Wished class: $wishedClassDated" }

        val decryptedUserName = encryptionService.decrypt(wishedClassDated.credentials.userName)
        val decryptedPassword = encryptionService.decrypt(wishedClassDated.credentials.password)

        pureDriverBuilder.build().use { pureDriver ->
            pureDriver.of(wishedClassDated.studio.url)
                    .performUserLogin(decryptedUserName, decryptedPassword)
                    .setAllLocation()
                    .setDate(wishedClassDated.dateTime)
                    .parse()

            //find matching candidates
            val matchingClasses = pureDriver.bookableClasses.filter { wishedClassDated.matches(it) }

            logger.info { "Found ${matchingClasses.size} matching classes:" }
            matchingClasses.forEach { logger.info { it } }

            return if (matchingClasses.size == 1) {
                pureDriver.book(matchingClasses.first())

            } else {
                logger.info { "There is not exactly 1 candidate for booking, nothing done" }
                "There is not exactly 1 candidate for booking, nothing done"
            }
        }
    }
}