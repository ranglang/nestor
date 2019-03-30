package org.gotson.nestor.interfaces.web

import org.gotson.nestor.domain.model.Membership
import org.gotson.nestor.domain.model.Studio
import org.gotson.nestor.domain.model.User
import org.gotson.nestor.domain.persistence.MembershipRepository
import org.gotson.nestor.domain.persistence.StudioRepository
import org.gotson.nestor.domain.persistence.UserRepository
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.AdditionalMatchers.not
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class MembershipControllerTest {
  @Autowired
  lateinit var mockMvc: MockMvc

  @MockBean
  lateinit var membershipRepository: MembershipRepository
  @MockBean
  lateinit var studioRepository: StudioRepository
  @MockBean
  lateinit var userRepository: UserRepository

  private val studio = Studio(1, "Pure Yoga", "http://pureyoga.com")
  private val george = User(2, "george@gmail.com", "George", "McKenzie", emptyList())
  private val membership = Membership(3, george, studio, "login", "password")

  private val route = "/membership"

  @Before
  fun setupMocks() {
    given(studioRepository.existsById(1)).willReturn(true)
    given(studioRepository.existsById(not(eq(1L)))).willReturn(false)
    given(studioRepository.findById(1)).willReturn(Optional.of(studio))

    given(userRepository.existsById(2)).willReturn(true)
    given(userRepository.existsById(not(eq(2L)))).willReturn(false)
    given(userRepository.findById(2)).willReturn(Optional.of(george))
  }

  companion object {
  }

  @Test
  fun `given incorrect id when getOne then return error not found`() {
    given(membershipRepository.findById(any())).willReturn(Optional.empty())

    mockMvc.perform(MockMvcRequestBuilders.get("$route/12"))
        .andExpect(MockMvcResultMatchers.status().isNotFound)
  }

  @Test
  fun `given membership id when getOne then return membership`() {
    given(membershipRepository.findById(3)).willReturn(Optional.of(membership))

    mockMvc.perform(MockMvcRequestBuilders.get("$route/3"))
        .andExpect(MockMvcResultMatchers.status().isOk)
        .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", equalTo(george.email)))
  }

  @Test
  fun `given membership without login when addOne then return bad request`() {
    val jsonString = """{"userId":"2", "studioId":"1", "password":"password"}"""

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(MockMvcResultMatchers.status().isBadRequest)
  }

  @Test
  fun `given membership with blank login when addOne then return bad request`() {
    val jsonString = """{"userId":"2", "studioId":"1", "login":"", "password":"password"}"""

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(MockMvcResultMatchers.status().isBadRequest)
  }

  @Test
  fun `given membership without password when addOne then return bad request`() {
    val jsonString = """{"userId":"2", "studioId":"1", "login":"login"}"""

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(MockMvcResultMatchers.status().isBadRequest)
  }

  @Test
  fun `given membership with blank password when addOne then return bad request`() {
    val jsonString = """{"userId":"2", "studioId":"1", "login":"login", "password":""}"""

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(MockMvcResultMatchers.status().isBadRequest)
  }

  @Test
  fun `given membership without userId when addOne then return bad request`() {
    val jsonString = """{"studioId":"1", "login":"login", "password":"password"}"""

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(MockMvcResultMatchers.status().isBadRequest)
  }

  @Test
  fun `given membership with invalid userId when addOne then return bad request`() {
    val jsonString = """{"userId":"20", "studioId":"1", "login":"login", "password":"password"}"""

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(MockMvcResultMatchers.status().isBadRequest)
  }

  @Test
  fun `given membership without studioId when addOne then return bad request`() {
    val jsonString = """{"userId":"2", "login":"login", "password":"password"}"""

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(MockMvcResultMatchers.status().isBadRequest)
  }

  @Test
  fun `given membership with invalid studioId when addOne then return bad request`() {
    val jsonString = """{"userId":"2", "studioId":"10", "login":"login", "password":"password"}"""

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(MockMvcResultMatchers.status().isBadRequest)
  }

  @Test
  fun `given existing membership when addOne then return bad request`() {
    given(membershipRepository.existsByUserIdAndStudioId(2, 1)).willReturn(true)

    val jsonString = """{"userId":"2", "studioId":"1", "login":"login", "password":"password"}"""

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(MockMvcResultMatchers.status().isBadRequest)
  }

  @Test
  fun `given valid membership when addOne then return membership`() {
    given(membershipRepository.existsByUserIdAndStudioId(2, 1)).willReturn(false)
    given<Membership>(membershipRepository.save(any())).willReturn(membership)

    val jsonString = """{"userId":"2", "studioId":"1", "login":"login", "password":"password"}"""

    mockMvc.perform(MockMvcRequestBuilders.post(route)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))
        .andExpect(MockMvcResultMatchers.status().isCreated)
  }
}