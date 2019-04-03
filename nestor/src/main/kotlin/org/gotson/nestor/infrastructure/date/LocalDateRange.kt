package org.gotson.nestor.infrastructure.date

import java.time.LocalDate

class LocalDateRange(
    override val start: LocalDate,
    override val endInclusive: LocalDate
) : ClosedRange<LocalDate>, Iterable<LocalDate> {

  override fun iterator(): Iterator<LocalDate> {
    return LocalDateIterator(start, endInclusive)
  }
}

class LocalDateIterator(start: LocalDate, private val endInclusive: LocalDate) : Iterator<LocalDate> {
  var current = start

  override fun hasNext(): Boolean = current <= endInclusive

  override fun next(): LocalDate = current++
}

operator fun LocalDate.inc(): LocalDate = this.plusDays(1)

operator fun LocalDate.rangeTo(that: LocalDate) = LocalDateRange(this, that)