package org.gotson.nestor.domain.model

import java.time.LocalDateTime

data class PlannedClass(
        val dateTime: LocalDateTime,
        val type: String,
        val instructor: String,
        val location: String,
        val bookingState: PlannedClassBookingState
)

enum class PlannedClassBookingState {
    CLOSED, OPEN, REGISTERED
}