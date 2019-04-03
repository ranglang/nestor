package org.gotson.nestor.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class PlannedClass(
    @Id
    @GeneratedValue
    val id: Long? = null,

    val date: LocalDate,
    val time: LocalTime,
    val instructor: String,
    val location: String,
    val type: String,
    val bookingState: PlannedClassBookingState,

    @ManyToOne(fetch = FetchType.EAGER)
    val studio: Studio
) {
  val dateTime: LocalDateTime
    get() = date.atTime(time)
}

enum class PlannedClassBookingState {
  CLOSED, OPEN, REGISTERED
}