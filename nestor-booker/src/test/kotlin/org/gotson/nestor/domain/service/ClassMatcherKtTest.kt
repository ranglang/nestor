package org.gotson.nestor.domain.service

import org.assertj.core.api.Assertions.assertThat
import org.gotson.nestor.domain.model.Credentials
import org.gotson.nestor.domain.model.PlannedClass
import org.gotson.nestor.domain.model.PlannedClassBookingState
import org.gotson.nestor.domain.model.Studio
import org.gotson.nestor.domain.model.User
import org.gotson.nestor.domain.model.WishedClassDated
import org.junit.Test
import java.time.LocalDateTime

class ClassMatcherKtTest {

    @Test
    fun `class matching is case insensitive`() {
        val planned = PlannedClass(
                dateTime = LocalDateTime.of(2018, 4, 29, 10, 15),
                type = "Yoga for Runners",
                instructor = "Priscilla",
                location = "Yoga - Hutchison House",
                bookingState = PlannedClassBookingState.OPEN)
        val wished = WishedClassDated(
                dateTime = LocalDateTime.of(2018, 4, 29, 10, 15),
                type = "yoga for runners",
                location = "hutchison house",
                studio = Studio(id = "123", name = "studio", url = "url"),
                credentials = Credentials("user", "pass"),
                user = User("123", "john@doe.com", "John", "Doe"))

        assertThat(wished.matches(planned)).isTrue()
    }

}