package org.gotson.nestor.domain.service

import mu.KotlinLogging
import org.gotson.nestor.domain.model.ClassRequest
import org.gotson.nestor.domain.model.dated
import org.gotson.nestor.domain.persistence.ClassRequestRepository
import org.gotson.nestor.domain.persistence.WeeklyClassRequestRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@Service
class ClassRequestFinder(
    private val weeklyClassRequestRepository: WeeklyClassRequestRepository,
    private val classRequestRepository: ClassRequestRepository,

    @Value("\${nestor.pure.advance-booking-days:2}")
    private val advanceBookingDays: Long
) {

  fun find(
      date: LocalDate = LocalDate.now().plusDays(advanceBookingDays)
  ): List<ClassRequest> {
    val weeklyClasses = weeklyClassRequestRepository.findByDay(date.dayOfWeek)
    val classes = classRequestRepository.findByDate(date)

    val allClasses = weeklyClasses.map { it.dated(date) } + classes
    logger.info { "Found ${allClasses.size} matching classes for date: $date" }
    return allClasses
  }

}
