package org.gotson.nestor.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class ClassRequest(
        val dateTime: LocalDateTime,
        val type: String,
        val location: String,
        val membership: Membership
) {
    fun summary(): String =
            "ClassRequest(dateTime=$dateTime, type=$type, location=$location)"
}

fun RecurringWishedClass.dated(date: LocalDate): ClassRequest =
        ClassRequest(
                dateTime = date.atTime(time),
                type = type,
                location = location,
                membership = membership
        )
