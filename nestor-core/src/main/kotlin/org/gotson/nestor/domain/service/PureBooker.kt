package org.gotson.nestor.domain.service

import mu.KotlinLogging
import org.gotson.nestor.domain.model.WishedClassDated
import org.gotson.nestor.domain.model.dated
import org.gotson.nestor.infrastructure.persistence.PersistenceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDate

private val logger = KotlinLogging.logger {}


@Service
class PureBooker @Autowired constructor(
        private val persistenceService: PersistenceService,

        @Value("\${nestor.pure.advance-booking-days:#{2}}")
        private val advanceBookingDays: Long
) {

    fun findMatchingWishedClasses(
            date: LocalDate = LocalDate.now().plusDays(advanceBookingDays)
    ): List<WishedClassDated> {
        val classes = persistenceService.findWishedClassByDay(date.dayOfWeek)
        logger.info { "Found ${classes.size} matching classes for date: $date" }
        return classes.map { it.dated(date) }
    }

}