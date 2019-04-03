package org.gotson.nestor.interfaces.scheduler

import org.assertj.core.api.Assertions.assertThat
import org.gotson.nestor.domain.model.ClassRequest
import org.gotson.nestor.domain.model.Membership
import org.gotson.nestor.domain.model.Studio
import org.gotson.nestor.domain.model.User
import org.gotson.nestor.domain.persistence.ClassRequestRepository
import org.gotson.nestor.domain.persistence.MembershipRepository
import org.gotson.nestor.domain.persistence.StudioRepository
import org.gotson.nestor.domain.persistence.UserRepository
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime

@ExtendWith(SpringExtension::class)
@SpringBootTest
class ScheduledControllerTest(
    @Autowired private val classRequestRepository: ClassRequestRepository,
    @Autowired private val membershipRepository: MembershipRepository,
    @Autowired private val studioRepository: StudioRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val scheduledController: ScheduledController
) {

  private val george = User(email = "george@gmail.com", firstName = "George", lastName = "McKenzie", icalCalendars = emptyList())
  private val pureYoga = Studio(name = "Pure Yoga", url = "http://pureyoga.com")
  private val membership = Membership(user = george, studio = pureYoga, login = "login", password = "password")

  @BeforeAll
  fun initData() {
    userRepository.save(george)
    studioRepository.save(pureYoga)
    membershipRepository.save(membership)
  }

  @AfterAll
  fun clearData() {
    classRequestRepository.deleteAll()
    membershipRepository.deleteAll()
    studioRepository.deleteAll()
    userRepository.deleteAll()
  }

  @Test
  @Transactional
  fun `given past and future requests when deletingOldRequests then only past requests are deleted`() {
    val yesterday = ClassRequest(
        membership = membership,
        date = LocalDate.now().minusDays(1),
        time = LocalTime.now(),
        location = "location",
        type = "type"
    )
    val today = ClassRequest(
        membership = membership,
        date = LocalDate.now(),
        time = LocalTime.now(),
        location = "location",
        type = "type"
    )
    val tomorrow = ClassRequest(
        membership = membership,
        date = LocalDate.now().plusDays(1),
        time = LocalTime.now(),
        location = "location",
        type = "type"
    )
    classRequestRepository.saveAll(listOf(yesterday, today, tomorrow))

    scheduledController.deletePastClassRequests()

    assertThat(classRequestRepository.count()).isEqualTo(2)
  }
}