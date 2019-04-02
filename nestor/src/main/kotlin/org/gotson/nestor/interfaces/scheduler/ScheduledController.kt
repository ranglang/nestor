package org.gotson.nestor.interfaces.scheduler

import mu.KotlinLogging
import org.gotson.nestor.domain.persistence.ClassRequestRepository
import org.gotson.nestor.domain.service.BookingService
import org.gotson.nestor.domain.service.ClassRequestFinder
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@Controller
class ScheduledController(
    private val classRequestFinder: ClassRequestFinder,
    private val bookingService: BookingService,
    private val classRequestRepository: ClassRequestRepository
) {

  @Scheduled(cron = "\${nestor.schedule.booking}")
  fun scheduledRun() {
    classRequestFinder.find().forEach { classRequest ->
      bookingService.bookPureYoga(classRequest)
    }
  }

  @Scheduled(cron = "\${nestor.schedule.deleteOld}")
  fun deleteOldClassRequest() {
    classRequestRepository.deleteByDateBefore(LocalDate.now())
  }
}


