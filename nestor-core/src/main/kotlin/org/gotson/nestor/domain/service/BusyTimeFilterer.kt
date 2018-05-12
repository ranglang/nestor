package org.gotson.nestor.domain.service

import org.gotson.nestor.domain.model.FilterResult
import org.gotson.nestor.domain.model.WishedClassDated
import org.gotson.nestor.infrastructure.calendar.IcalRetriever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class BusyTimeFilterer @Autowired constructor(
    private val icalRetriever: IcalRetriever

) {
  fun filterRequest(bookingRequest: WishedClassDated): FilterResult {
    val busyTimes = bookingRequest.user.icalCalendars.flatMap {
      icalRetriever.getCalendar(it)
    }

    val res = busyTimes.firstOrNull {
      bookingRequest.dateTime.toLocalDate().isWithinRange(it.startDate, it.endDate)
    }

    return if (res == null)
      FilterResult(false, null)
    else
      FilterResult(true, res)
  }
}

fun LocalDate.isWithinRange(start: LocalDate, end: LocalDate): Boolean {
  return (this.isEqual(start) || this.isAfter(start))
      && (this.isEqual(end) || this.isBefore(end))
}