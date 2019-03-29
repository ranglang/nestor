package org.gotson.nestor.domain.model

class ScheduleConflictException(
        message: String,
        val busyTime: BusyTime
) : Exception(message)