package org.gotson.nestor.interfaces.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.gotson.nestor.domain.model.Membership
import org.gotson.nestor.domain.model.Studio
import org.gotson.nestor.domain.model.User
import org.gotson.nestor.domain.persistence.MembershipRepository
import org.gotson.nestor.domain.persistence.StudioRepository
import org.gotson.nestor.domain.persistence.UserRepository
import org.hamcrest.Matchers.equalTo
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class MembershipControllerTest(
    @Autowired private val mockMvc: MockMvc
) {
  @MockkBean
  private lateinit var membershipRepository: MembershipRepository
  @MockkBean
  private lateinit var studioRepository: StudioRepository
  @MockkBean
  private lateinit var userRepository: UserRepository

  private val studio = Studio(1, "Pure Yoga", "http://pureyoga.com")
  private val george = User(2, "george@gmail.com", "George", "McKenzie", emptyList())
  private val membership = Membership(3, george, studio, "login", "password")

  private val route = "/memberships"

  @BeforeEach
  fun setupMocks() {
    every { studioRepository.existsById(1) } returns true
    every { studioRepository.existsById(neq(1)) } returns false
    every { studioRepository.findById(1) } returns Optional.of(studio)
    every { studioRepository.findById(neq(1)) } returns Optional.empty()

    every { userRepository.existsById(2) } returns true
    every { userRepository.existsById(neq(2)) } returns false
    every { userRepository.findById(2) } returns Optional.of(george)
    every { userRepository.findById(neq(2)) } returns Optional.empty()

    every { membershipRepository.existsByUserIdAndStudioId(any(), any()) } returns false
  }

  @Nested
  inner class GetOne {
    @Test
    fun `given incorrect id when getOne then return error not found`() {
      every { membershipRepository.findById(any()) } returns Optional.empty()

      mockMvc.perform(MockMvcRequestBuilders.get("$route/12"))
          .andExpect(status().isNotFound)
    }

    @Test
    fun `given membership id when getOne then return membership`() {
      every { membershipRepository.findById(3) } returns Optional.of(membership)

      mockMvc.perform(MockMvcRequestBuilders.get("$route/3"))
          .andExpect(status().isOk)
          .andExpect(jsonPath("$.id", equalTo(membership.id?.toInt())))
    }
  }

  @Nested
  inner class AddOne {
    @Test
    fun `given membership without login when addOne then return bad request`() {
      val jsonString = """{"userId":"2", "studioId":"1", "password":"password"}"""

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given membership with blank login when addOne then return bad request`() {
      val jsonString = """{"userId":"2", "studioId":"1", "login":"", "password":"password"}"""

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given membership without password when addOne then return bad request`() {
      val jsonString = """{"userId":"2", "studioId":"1", "login":"login"}"""

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given membership with blank password when addOne then return bad request`() {
      val jsonString = """{"userId":"2", "studioId":"1", "login":"login", "password":""}"""

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given membership without userId when addOne then return bad request`() {
      val jsonString = """{"studioId":"1", "login":"login", "password":"password"}"""

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given membership with invalid userId when addOne then return bad request`() {
      val jsonString = """{"userId":"20", "studioId":"1", "login":"login", "password":"password"}"""

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given membership without studioId when addOne then return bad request`() {
      val jsonString = """{"userId":"2", "login":"login", "password":"password"}"""

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given membership with invalid studioId when addOne then return bad request`() {
      val jsonString = """{"userId":"2", "studioId":"10", "login":"login", "password":"password"}"""

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given existing membership when addOne then return bad request`() {
      every { membershipRepository.existsByUserIdAndStudioId(2, 1) } returns true

      val jsonString = """{"userId":"2", "studioId":"1", "login":"login", "password":"password"}"""

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given valid membership when addOne then return membership`() {
      every { membershipRepository.existsByUserIdAndStudioId(2, 1) } returns false
      every { membershipRepository.save(ofType(Membership::class)) } returns membership

      val jsonString = """{"userId":"2", "studioId":"1", "login":"login", "password":"password"}"""

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isCreated)
    }
  }
}