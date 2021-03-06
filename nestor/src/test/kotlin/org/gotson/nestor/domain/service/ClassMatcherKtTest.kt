package org.gotson.nestor.domain.service

import org.assertj.core.api.Assertions.assertThat
import org.gotson.nestor.domain.model.ClassRequest
import org.gotson.nestor.domain.model.Membership
import org.gotson.nestor.domain.model.PlannedClass
import org.gotson.nestor.domain.model.PlannedClassBookingState
import org.gotson.nestor.domain.model.Studio
import org.gotson.nestor.domain.model.User
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ClassMatcherKtTest {

  @Test
  fun `class matching is case insensitive`() {
    val planned = PlannedClass(
        date = LocalDate.of(2018, 4, 29),
        time = LocalTime.of(10, 15),
        type = "Yoga for Runners",
        instructor = "Priscilla",
        location = "Yoga - Hutchison House",
        bookingState = PlannedClassBookingState.OPEN,
        studio = Studio(id = 123, name = "studio", url = "url"))
    val wished = ClassRequest(
        date = LocalDate.of(2018, 4, 29),
        time = LocalTime.of(10, 15),
        type = "yoga for runners",
        location = "hutchison house",
        membership = Membership(
            id = 123,
            user = User(123, "john@doe.com", "John", "Doe"),
            studio = Studio(id = 123, name = "studio", url = "url"),
            login = "user",
            password = "pass"
        )
    )

    assertThat(wished.matches(planned)).isTrue()
  }

}