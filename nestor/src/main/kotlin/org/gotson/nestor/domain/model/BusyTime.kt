package org.gotson.nestor.domain.model

import java.time.LocalDate

data class BusyTime(
        val startDate: LocalDate,
        val endDate: LocalDate,
        val summary: String
)