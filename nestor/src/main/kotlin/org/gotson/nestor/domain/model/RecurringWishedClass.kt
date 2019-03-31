package org.gotson.nestor.domain.model

import java.time.DayOfWeek
import java.time.LocalTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class RecurringWishedClass(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    val membership: Membership,
    val time: LocalTime,
    val day: DayOfWeek,
    val location: String,
    val type: String
)