package org.gotson.nestor.domain.model

import java.time.DayOfWeek
import java.time.LocalTime

data class WishedClass(
        val id: String? = null,
        val membership: Membership,
        val time: LocalTime,
        val day: DayOfWeek,
        val location: String,
        val type: String
)