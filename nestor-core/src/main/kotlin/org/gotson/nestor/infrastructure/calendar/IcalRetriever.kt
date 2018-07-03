package org.gotson.nestor.infrastructure.calendar

import biweekly.Biweekly
import mu.KotlinLogging
import org.gotson.nestor.domain.model.BusyTime
import org.springframework.stereotype.Service
import java.net.URL
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class IcalRetriever {

  fun getCalendar(icalUrl: String): List<BusyTime> {
    try {
      URL(icalUrl).openStream().use {
        val calendar = Biweekly.parse(it).first()

        val allDayEvents = calendar.events
            .filter { !it.dateStart.value.hasTime() }
            .filter { it.dateEnd == null || !it.dateEnd.value.hasTime() }

        return allDayEvents.map {
          val actualEnd = if (it.dateEnd == null) {
            it.dateStart.value.convertToLocalDateViaInstant()
          } else {
            it.dateEnd.value.convertToLocalDateViaInstant().minusDays(1)
          }

          BusyTime(
              it.dateStart.value.convertToLocalDateViaInstant(),
              actualEnd,
              it.summary.value)
        }
      }
    } catch (e: Exception) {
      logger.error(e) { "Error while retrieving calendar: $icalUrl" }
      return emptyList()
    }
  }
}

fun Date.convertToLocalDateViaInstant(): LocalDate {
  return this.toInstant()
      .atZone(ZoneId.systemDefault())
      .toLocalDate()
}