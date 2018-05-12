package org.gotson.nestor

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("testtrigger")
class AutowiringTriggerTest {

  @Test
  fun `Application loads properly with trigger properties`() {

  }
}

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("testcalendar")
class AutowiringCalendarCheckTest {

  @Test
  fun `Application loads properly with calendar-check properties`() {
  }
}