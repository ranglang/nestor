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
data class ClassRequest(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    val membership: Membership,
    val date: LocalDate,
    val time: LocalTime,
    val location: String,
    val type: String
) {
  val dateTime: LocalDateTime
    get() = date.atTime(time)

  fun summary(): String =
      "ClassRequest(dateTime=$dateTime, type=$type, location=$location)"
}

fun WeeklyClassRequest.dated(date: LocalDate): ClassRequest =
    ClassRequest(
        date = date,
        time = time,
        type = type,
        location = location,
        membership = membership
    )
