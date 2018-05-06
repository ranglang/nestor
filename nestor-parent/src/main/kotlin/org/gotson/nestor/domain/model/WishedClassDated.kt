package org.gotson.nestor.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class WishedClassDated(
        val dateTime: LocalDateTime,
        val type: String,
        val location: String,
        val studio: Studio,
        val credentials: Credentials,
        val user: User
) {
    fun summary(): String =
            "WishedClassDated(dateTime=$dateTime, type=$type, location=$location)"
}

fun WishedClass.dated(date: LocalDate): WishedClassDated =
        WishedClassDated(
                dateTime = date.atTime(time),
                type = type,
                location = location,
                studio = membership.studio,
                credentials = Credentials(membership.login, membership.password),
                user = membership.user
        )
