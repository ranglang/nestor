package org.gotson.nestor.interfaces.scheduler

import mu.KotlinLogging
import org.gotson.nestor.domain.persistence.ClassRequestRepository
import org.gotson.nestor.domain.persistence.PlannedClassRepository
import org.gotson.nestor.domain.persistence.StudioRepository
import org.gotson.nestor.domain.service.BookingService
import org.gotson.nestor.domain.service.ClassRequestFinder
import org.gotson.nestor.domain.service.StudioClassRetriever
import org.gotson.nestor.infrastructure.date.rangeTo
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@Controller
class ScheduledController(
    private val classRequestFinder: ClassRequestFinder,
    private val bookingService: BookingService,
    private val classRequestRepository: ClassRequestRepository,
    private val studioRepository: StudioRepository,
    private val plannedClassRepository: PlannedClassRepository,
    private val studioClassRetriever: StudioClassRetriever,

    @Value("\${nestor.updateClasses.daysFrom:3}")
    private val updateClassesDayFrom: Long,

    @Value("\${nestor.updateClasses.daysTo:10}")
    private val updateClassesDayTo: Long
) {

  @Scheduled(cron = "\${nestor.schedule.bookClassRequests}")
  fun bookClassRequests() {
    classRequestFinder.find().forEach { classRequest ->
      bookingService.bookPureYoga(classRequest)
    }
  }

  @Scheduled(cron = "\${nestor.schedule.deletePastClassRequests}")
  fun deletePastClassRequests() {
    classRequestRepository.deleteByDateBefore(LocalDate.now())
  }

  @Scheduled(cron = "\${nestor.schedule.updateFutureClasses}")
  fun updateFutureClassesForAllStudios() {
    val dateFrom = LocalDate.now().plusDays(updateClassesDayFrom)
    val dateTo = LocalDate.now().plusDays(updateClassesDayTo)

    studioRepository.findAll().forEach { studio ->
      logger.info { "Retrieving classes from $dateFrom to $dateTo for Studio ${studio.name}" }
      val plannedClasses = studioClassRetriever.retrieveAll(studio, (dateFrom..dateTo).toList())

      logger.info { "Retrieved ${plannedClasses.size} planned classes, updating database now" }

      plannedClassRepository.deleteByDateBetweenAndStudio(dateFrom, dateTo, studio)
      plannedClassRepository.saveAll(plannedClasses)
    }
  }
}


