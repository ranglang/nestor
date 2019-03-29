package org.gotson.nestor.domain.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.LocalDate

class BusyTimeFiltererKtTest {

    @Test
    fun `date is within range but outside bounds`() {
        val start = LocalDate.of(2018, 1, 8)
        val end = LocalDate.of(2018, 1, 10)
        val dateTest = LocalDate.of(2018, 1, 9)


        val result = dateTest.isWithinRange(start, end)

        assertThat(result).isTrue()
    }

    @Test
    fun `date is within range and equals start bound`() {
        val start = LocalDate.of(2018, 1, 8)
        val end = LocalDate.of(2018, 1, 10)
        val dateTest = LocalDate.of(2018, 1, 8)


        val result = dateTest.isWithinRange(start, end)

        assertThat(result).isTrue()
    }

    @Test
    fun `date is within range and equals end bound`() {
        val start = LocalDate.of(2018, 1, 8)
        val end = LocalDate.of(2018, 1, 10)
        val dateTest = LocalDate.of(2018, 1, 10)


        val result = dateTest.isWithinRange(start, end)

        assertThat(result).isTrue()
    }

    @Test
    fun `date is within single day range`() {
        val start = LocalDate.of(2018, 1, 8)
        val end = LocalDate.of(2018, 1, 8)
        val dateTest = LocalDate.of(2018, 1, 8)


        val result = dateTest.isWithinRange(start, end)

        assertThat(result).isTrue()
    }

    @Test
    fun `date is before range`() {
        val start = LocalDate.of(2018, 1, 8)
        val end = LocalDate.of(2018, 1, 10)
        val dateTest = LocalDate.of(2018, 1, 5)


        val result = dateTest.isWithinRange(start, end)

        assertThat(result).isFalse()
    }

    @Test
    fun `date is after range`() {
        val start = LocalDate.of(2018, 1, 8)
        val end = LocalDate.of(2018, 1, 10)
        val dateTest = LocalDate.of(2018, 1, 15)


        val result = dateTest.isWithinRange(start, end)

        assertThat(result).isFalse()
    }
}