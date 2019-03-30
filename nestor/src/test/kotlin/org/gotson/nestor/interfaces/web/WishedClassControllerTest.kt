package org.gotson.nestor.interfaces.web

import org.gotson.nestor.domain.model.Membership
import org.gotson.nestor.domain.model.RecurringWishedClass
import org.gotson.nestor.domain.model.Studio
import org.gotson.nestor.domain.model.User
import org.gotson.nestor.domain.persistence.MembershipRepository
import org.gotson.nestor.domain.persistence.RecurringWishedClassRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class WishedClassControllerTest {
  @Autowired
  lateinit var mockMvc: MockMvc

  @MockBean
  lateinit var repository: RecurringWishedClassRepository

  @MockBean
  lateinit var membershipRepository: MembershipRepository

  private val membership = Membership(
      1,
      User(2, "george@gmail.com", "George", "McKenzie", emptyList()),
      Studio(1, "Pure Yoga", "http://pureyoga.com"),
      "login",
      "password"
  )

  private val route = "/wishedclass"

  @Before
  fun initMocks() {
    given(membershipRepository.findById(1)).willReturn(Optional.of(membership))
  }

  @Test
  fun `given wishedClass without time when addOne then return bad request`() {
    val jsonString = """{
      "membershipId":"1",
      "day":"MONDAY",
      "location":"pacific",
      "type":"hatha"
      }""".trimIndent()

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(status().isBadRequest)
  }

  @Test
  fun `given wishedClass with blank time when addOne then return bad request`() {
    val jsonString = """{
      "membershipId":"1",
      "time":"",
      "day":"MONDAY",
      "location":"pacific",
      "type":"hatha"
      }""".trimIndent()

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(status().isBadRequest)
  }

  @Test
  fun `given wishedClass without day when addOne then return bad request`() {
    val jsonString = """{
      "membershipId":"1",
      "time":"12:00",
      "location":"pacific",
      "type":"hatha"
      }""".trimIndent()

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(status().isBadRequest)
  }

  @Test
  fun `given wishedClass with blank day when addOne then return bad request`() {
    val jsonString = """{
      "membershipId":"1",
      "time":"12:00",
      "day":"",
      "location":"pacific",
      "type":"hatha"
      }""".trimIndent()

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(status().isBadRequest)
  }

  @Test
  fun `given wishedClass without location when addOne then return bad request`() {
    val jsonString = """{
      "membershipId":"1",
      "time":"12:00",
      "day":"MONDAY",
      "type":"hatha"
      }""".trimIndent()

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(status().isBadRequest)
  }

  @Test
  fun `given wishedClass with blank location when addOne then return bad request`() {
    val jsonString = """{
      "membershipId":"1",
      "time":"12:00",
      "day":"MONDAY",
      "location":"",
      "type":"hatha"
      }""".trimIndent()

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(status().isBadRequest)
  }

  @Test
  fun `given wishedClass without type when addOne then return bad request`() {
    val jsonString = """{
      "membershipId":"1",
      "time":"12:00",
      "day":"MONDAY",
      "location":"pacific"
      }""".trimIndent()

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(status().isBadRequest)
  }

  @Test
  fun `given wishedClass with blank type when addOne then return bad request`() {
    val jsonString = """{
      "membershipId":"1",
      "time":"12:00",
      "day":"MONDAY",
      "location":"pacific",
      "type":""
      }""".trimIndent()

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(status().isBadRequest)
  }

  @Test
  fun `given wishedClass without membershipId when addOne then return bad request`() {
    val jsonString = """{
      "time":"12:00",
      "day":"MONDAY",
      "location":"pacific",
      "type":"hatha"
      }""".trimIndent()

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(status().isBadRequest)
  }

  @Test
  fun `given wishedClass with invalid membershipId when addOne then return bad request`() {
    val jsonString = """{
      "membershipId":"10",
      "time":"12:00",
      "day":"MONDAY",
      "location":"pacific",
      "type":"hatha"
      }""".trimIndent()

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(status().isBadRequest)
  }

  @Test
  fun `given valid wishedClass when addOne then return created`() {
    given<RecurringWishedClass>(repository.save(any())).willReturn(
        RecurringWishedClass(
            1,
            membership,
            LocalTime.of(12, 0, 0),
            DayOfWeek.MONDAY,
            "pacific",
            "hatha"
        )
    )

    val jsonString = """{
      "membershipId":"1",
      "time":"12:00",
      "day":"MONDAY",
      "location":"pacific",
      "type":"hatha"
      }""".trimIndent()

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(status().isCreated)
  }
}