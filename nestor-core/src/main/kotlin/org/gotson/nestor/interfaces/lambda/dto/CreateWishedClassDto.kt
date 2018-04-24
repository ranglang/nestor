package org.gotson.nestor.interfaces.lambda.dto

import java.time.DayOfWeek
import java.time.LocalTime

data class CreateWishedClassDto(
        val membershipId: String,
        val time: LocalTime,
        val day: DayOfWeek,
        val location: String,
        val type: String
)