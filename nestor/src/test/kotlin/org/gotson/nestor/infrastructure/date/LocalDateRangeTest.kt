package org.gotson.nestor.infrastructure.date

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LocalDateRangeTest {
  @Test
  fun `given same bounds then return list of 1 element`() {
    val date = LocalDate.of(2019, 1, 1)

    val dateList = (date..date).toList()

    assertThat(dateList)
        .hasSize(1)
        .containsOnly(date)
  }

  @Test
  fun `given dateTo before dateFrom then return empty list`() {
    val dateFrom = LocalDate.of(2019, 2, 1)
    val dateTo = LocalDate.of(2019, 1, 1)

    val dateList = (dateFrom..dateTo).toList()

    assertThat(dateList).isEmpty()
  }

  @Test
  fun `given consecutive dates then return list of 2 elements`() {
    val dateFrom = LocalDate.of(2019, 1, 1)
    val dateTo = LocalDate.of(2019, 1, 2)

    val dateList = (dateFrom..dateTo).toList()

    assertThat(dateList)
        .hasSize(2)
        .containsExactly(dateFrom, dateTo)
  }

  @Test
  fun `given dateFrom before dateTo then return list of dates between inclusive`() {
    val dateFrom = LocalDate.of(2019, 1, 1)
    val dateTo = LocalDate.of(2019, 1, 4)

    val dateList = (dateFrom..dateTo).toList()

    assertThat(dateList)
        .hasSize(4)
        .containsExactly(dateFrom, dateFrom.plusDays(1), dateFrom.plusDays(2), dateTo)
  }
}