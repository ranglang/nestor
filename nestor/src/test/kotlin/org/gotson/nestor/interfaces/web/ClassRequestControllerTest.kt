package org.gotson.nestor.interfaces.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.gotson.nestor.domain.model.ClassRequest
import org.gotson.nestor.domain.model.Membership
import org.gotson.nestor.domain.model.Studio
import org.gotson.nestor.domain.model.User
import org.gotson.nestor.domain.persistence.ClassRequestRepository
import org.gotson.nestor.domain.persistence.MembershipRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class ClassRequestControllerTest(
    @Autowired private val mockMvc: MockMvc
) {
  @MockkBean
  private lateinit var repository: ClassRequestRepository

  @MockkBean
  private lateinit var membershipRepository: MembershipRepository

  private val membership = Membership(
      1,
      User(2, "george@gmail.com", "George", "McKenzie", emptyList()),
      Studio(1, "Pure Yoga", "http://pureyoga.com"),
      "login",
      "password"
  )

  private val route = "/classrequests"

  @BeforeEach
  fun initMocks() {
    every { membershipRepository.findById(1) } returns Optional.of(membership)
    every { membershipRepository.findById(neq(1)) } returns Optional.empty()
  }

  @Nested
  inner class addOne {
    @Test
    fun `given classRequest without time when addOne then return bad request`() {
      val date = LocalDate.now().plusDays(1)
      val jsonString = """{
      "membershipId":"1",
      "date":"$date",
      "location":"pacific",
      "type":"hatha"
      }""".trimIndent()

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given classRequest with blank time when addOne then return bad request`() {
      val date = LocalDate.now().plusDays(1)
      val jsonString = """{
      "membershipId":"1",
      "time":"",
      "date":"$date",
      "location":"pacific",
      "type":"hatha"
      }""".trimIndent()

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given classRequest without date when addOne then return bad request`() {
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
    fun `given classRequest with blank date when addOne then return bad request`() {
      val jsonString = """{
      "membershipId":"1",
      "time":"12:00",
      "date":"",
      "location":"pacific",
      "type":"hatha"
      }""".trimIndent()

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given classRequest with past date when addOne then return bad request`() {
      val date = LocalDate.now().minusDays(1)
      val jsonString = """{
      "membershipId":"1",
      "time":"12:00",
      "date":"$date",,
      "location":"pacific",
      "type":"hatha"
      }""".trimIndent()

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given classRequest without location when addOne then return bad request`() {
      val date = LocalDate.now().plusDays(1)
      val jsonString = """{
      "membershipId":"1",
      "time":"12:00",
      "date":"$date",
      "type":"hatha"
      }""".trimIndent()

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given classRequest with blank location when addOne then return bad request`() {
      val date = LocalDate.now().plusDays(1)
      val jsonString = """{
      "membershipId":"1",
      "time":"12:00",
      "date":"$date",
      "location":"",
      "type":"hatha"
      }""".trimIndent()

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given classRequest without type when addOne then return bad request`() {
      val date = LocalDate.now().plusDays(1)
      val jsonString = """{
      "membershipId":"1",
      "time":"12:00",
      "date":"$date",
      "location":"pacific"
      }""".trimIndent()

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given classRequest with blank type when addOne then return bad request`() {
      val date = LocalDate.now().plusDays(1)
      val jsonString = """{
      "membershipId":"1",
      "time":"12:00",
      "date":"$date",
      "location":"pacific",
      "type":""
      }""".trimIndent()

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given classRequest without membershipId when addOne then return bad request`() {
      val date = LocalDate.now().plusDays(1)
      val jsonString = """{
      "time":"12:00",
      "date":"$date",
      "location":"pacific",
      "type":"hatha"
      }""".trimIndent()

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given classRequest with invalid membershipId when addOne then return bad request`() {
      val date = LocalDate.now().plusDays(1)
      val jsonString = """{
      "membershipId":"10",
      "time":"12:00",
      "date":"$date",
      "location":"pacific",
      "type":"hatha"
      }""".trimIndent()

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given valid classRequest when addOne then return created`() {
      val date = LocalDate.now().plusDays(1)
      every { repository.save(ofType(ClassRequest::class)) } returns
          ClassRequest(
              id = 1,
              membership = membership,
              date = date,
              time = LocalTime.of(12, 0, 0),
              location = "pacific",
              type = "hatha"
          )


      val jsonString = """{
      "membershipId":"1",
      "time":"12:00",
      "date":"$date",
      "location":"pacific",
      "type":"hatha"
      }""".trimIndent()

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isCreated)
    }
  }
}