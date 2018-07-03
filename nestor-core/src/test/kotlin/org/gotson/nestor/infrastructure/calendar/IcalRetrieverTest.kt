package org.gotson.nestor.infrastructure.calendar

import org.assertj.core.api.Assertions.assertThat
import org.gotson.nestor.domain.model.BusyTime
import org.junit.Test
import org.springframework.core.io.ClassPathResource
import java.time.LocalDate

class IcalRetrieverTest {

  @Test
  fun `ICS event with no time and no DTEND is considered as one day full day event`() {
    val filePath = ClassPathResource("calendar/one-day-no-dtend.ics").file.toURI().toURL().toString()

    val result = IcalRetriever().getCalendar(filePath)

    assertThat(result).containsExactly(
        BusyTime(
            LocalDate.of(2018, 4, 1),
            LocalDate.of(2018, 4, 1),
            "Test1"
        )
    )
  }

  @Test
  fun `ICS event with no time but with DTEND is considered as one day full day event`() {
    val filePath = ClassPathResource("calendar/one-day-with-dtend.ics").file.toURI().toURL().toString()

    val result = IcalRetriever().getCalendar(filePath)

    assertThat(result).containsExactly(
        BusyTime(
            LocalDate.of(2018, 4, 1),
            LocalDate.of(2018, 4, 1),
            "Test2"
        )
    )
  }

  @Test
  fun `ICS full-day event should not include the end date as part of the busy time`() {
    val filePath = ClassPathResource("calendar/two-days.ics").file.toURI().toURL().toString()

    val result = IcalRetriever().getCalendar(filePath)

    assertThat(result).containsExactly(
        BusyTime(
            LocalDate.of(2018, 4, 1),
            LocalDate.of(2018, 4, 2),
            "Test3"
        )
    )
  }
}