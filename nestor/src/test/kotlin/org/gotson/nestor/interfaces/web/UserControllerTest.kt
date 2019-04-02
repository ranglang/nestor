package org.gotson.nestor.interfaces.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.gotson.nestor.domain.model.User
import org.gotson.nestor.domain.persistence.UserRepository
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class UserControllerTest(
    @Autowired private val mockMvc: MockMvc
) {
  @MockkBean
  private lateinit var userRepository: UserRepository

  private val jacky = User(1, "jacky@gmail.com", "Jacky", "Boulet", emptyList())
  private val george = User(2, "george@gmail.com", "George", "McKenzie", emptyList())

  private val route = "/users"

  @Nested
  inner class GetAll {
    @Test
    fun `given no user when getAll then return empty list`() {
      every { userRepository.findAll() } returns emptyList()

      mockMvc.perform(get(route))
          .andExpect(status().isOk)
          .andExpect(jsonPath("$", hasSize<Any>(0)))
    }

    @Test
    fun `given a user when getAll then return single user in list`() {
      every { userRepository.findAll() } returns listOf(jacky)

      mockMvc.perform(get(route))
          .andExpect(status().isOk)
          .andExpect(jsonPath("$", hasSize<Any>(1)))
          .andExpect(jsonPath("$[0].firstName", equalTo(jacky.firstName)))
    }
  }

  @Nested
  inner class GetOne {
    @Test
    fun `given incorrect id when getOne then return error not found`() {
      every { userRepository.findById(any()) } returns Optional.empty()

      mockMvc.perform(get("$route/12"))
          .andExpect(status().isNotFound)
    }

    @Test
    fun `given user id when getOne then return user`() {
      every { userRepository.findById(2) } returns Optional.of(george)

      mockMvc.perform(get("$route/2"))
          .andExpect(status().isOk)
          .andExpect(jsonPath("$.firstName", equalTo(george.firstName)))
    }
  }

  @Nested
  inner class AddOne {
    @Test
    fun `given user without firstname when addOne then return bad request`() {
      val jsonString = """{"email":"juste.leblanc@gmail.com", "lastName":"LeBlanc"}"""

      mockMvc.perform(post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given user with blank firstname when addOne then return bad request`() {
      val jsonString = """{"email":"juste.leblanc@gmail.com", "firstName":"", "lastName":"LeBlanc"}"""

      mockMvc.perform(post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given user without lastname when addOne then return bad request`() {
      val jsonString = """{"email":"juste.leblanc@gmail.com", "firstName":"Juste"}"""

      mockMvc.perform(post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given user with blank lastname when addOne then return bad request`() {
      val jsonString = """{"email":"juste.leblanc@gmail.com", "firstName":"Juste", "lastName":""}"""

      mockMvc.perform(post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given user with malformed email when addOne then return bad request`() {
      val jsonString = """{"email":"juste.leblancATgmail.com", "firstName":"Juste", "lastName":"LeBlanc"}"""

      mockMvc.perform(post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given user with email already existing when addOne then return bad request`() {
      val email = "juste.leblanc@gmail.com"
      val jsonString = """{"email":"$email", "firstName":"Juste", "lastName":"LeBlanc"}"""

      every { userRepository.existsByEmail(email) } returns true

      mockMvc.perform(post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given valid user when addOne then return user`() {
      val email = "juste.leblanc@gmail.com"
      val jsonString = """{"email":"$email", "firstName":"Juste", "lastName":"LeBlanc"}"""

      every { userRepository.existsByEmail(email) } returns false
      every { userRepository.save(ofType(User::class)) } returns User(1, email, "Juste", "LeBlanc", emptyList())

      mockMvc.perform(post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isCreated)
          .andExpect(jsonPath("$.firstName", equalTo("Juste")))
    }
  }
}