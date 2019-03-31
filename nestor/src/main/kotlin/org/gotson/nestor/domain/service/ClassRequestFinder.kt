package org.gotson.nestor.domain.service

import mu.KotlinLogging
import org.gotson.nestor.domain.model.ClassRequest
import org.gotson.nestor.domain.model.dated
import org.gotson.nestor.domain.persistence.RecurringWishedClassRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@Service
class ClassRequestFinder(
    private val recurringWishedClassRepository: RecurringWishedClassRepository,

    @Value("\${nestor.pure.advance-booking-days:2}")
    private val advanceBookingDays: Long
) {

  fun findRecurring(
      date: LocalDate = LocalDate.now().plusDays(advanceBookingDays)
  ): List<ClassRequest> {
    val classes = recurringWishedClassRepository.findByDay(date.dayOfWeek)
    logger.info { "Found ${classes.size} matching classes for date: $date" }
    return classes.map { it.dated(date) }
  }

}
