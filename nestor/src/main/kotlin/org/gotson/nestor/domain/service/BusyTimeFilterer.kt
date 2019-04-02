package org.gotson.nestor.domain.service

import org.gotson.nestor.domain.model.ClassRequest
import org.gotson.nestor.domain.model.ScheduleConflictException
import org.gotson.nestor.infrastructure.calendar.IcalRetriever
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class BusyTimeFilterer constructor(
    private val icalRetriever: IcalRetriever

) {
  fun checkForConflicts(classRequest: ClassRequest) {
    val busyTimes = classRequest.membership.user.icalCalendars.flatMap {
      icalRetriever.getBusyTimes(it)
    }

    busyTimes.firstOrNull {
      classRequest.date.isWithinRange(it.startDate, it.endDate)
    }?.let {
      throw ScheduleConflictException("Conflicting schedule", it)
    }
  }
}

fun LocalDate.isWithinRange(start: LocalDate, end: LocalDate): Boolean {
  return (this.isEqual(start) || this.isAfter(start))
      && (this.isEqual(end) || this.isBefore(end))
}