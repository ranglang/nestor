package org.gotson.nestor.interfaces.lambda.api.dto

import org.gotson.nestor.domain.model.WishedClass
import java.time.DayOfWeek
import java.time.LocalTime

data class WishedClassDto(
        val id: String,
        val membership: MembershipDto,
        val time: LocalTime,
        val day: DayOfWeek,
        val location: String,
        val type: String
)

fun WishedClass.toDto(): WishedClassDto =
        WishedClassDto(
                id!!,
                membership.toDto(),
                time,
                day,
                location,
                type
        )