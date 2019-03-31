package org.gotson.nestor.interfaces.scheduler

import mu.KotlinLogging
import org.gotson.nestor.domain.service.BookingService
import org.gotson.nestor.domain.service.ClassRequestFinder
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller

private val logger = KotlinLogging.logger {}

@Controller
class ScheduledController(
    private val classRequestFinder: ClassRequestFinder,
    private val bookingService: BookingService
) {

  @Scheduled(cron = "\${nestor.schedule}")
  fun scheduledRun() {
    classRequestFinder.findRecurring().forEach { classRequest ->
      bookingService.bookPureYoga(classRequest)
    }
  }

}


